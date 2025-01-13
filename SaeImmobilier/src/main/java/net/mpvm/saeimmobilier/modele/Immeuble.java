package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.SQLException;
import java.util.*;
import java.sql.Date;

public final class Immeuble extends Bien{

	public static final String INSERT_QUERY = "INSERT INTO Bien (Adresse, Ville, CodePostal, TypeBien, NumeroFiscal, IdProprio, DateAjout) VALUES (?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM Bien WHERE TypeBien = 'IMMEUBLE'";
	public static final String SELECT_WHERE_QUERY = "SELECT * FROM Bien WHERE Adresse = ? AND Ville = ? AND CodePostal = ?";
	public static final String DELETE_QUERY = "DELETE FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String UPDATE_QUERY = "UPDATE Bien SET Adresse = ?, Ville = ?, CodePostal = ?, IdProprio = ? WHERE IdBien = ?";
	public static final String SELECT_FROM_ID = "SELECT * FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String SELECT_BIENS_IMMEUBLES = "SELECT * FROM Bien WHERE IdImmeuble = ? AND TypeBien = 'GARAGE' OR TypeBien = 'HABITATION'";
	public static final String SELECT_COUNT_BL = "SELECT Count(*) FROM Bien WHERE IdImmeuble = ? AND (TypeBien = 'GARAGE' OR TypeBien = 'HABITATION')";
	public static final String SELECT_LOCALISATION = "SELECT IdImmeuble FROM Bien WHERE Adresse = ? AND Ville = ? AND CodePostal = ? AND TypeBien = 'IMMEUBLE'";

	private String adresse;
	private String ville;
	private String codePostal;

	private Immeuble(String ville, String codePostal, String adresse, String numeroFiscal, Date dateAjout, String idProprio, int idBien) {
		super(idBien, numeroFiscal, dateAjout, idProprio); // Initialisation des attributs hérités de Bien
		this.codePostal = codePostal;
		this.adresse = adresse;
		this.ville = ville;
	}

	private Immeuble(IBuilder iBuilder) {
		this(iBuilder.ville, iBuilder.codePostal, iBuilder.adresse, iBuilder.getNumeroFiscal(), iBuilder.getDateAjout(), iBuilder.getIdProprio(), iBuilder.getIdBien());
	}

	public static Immeuble findByLocalisation(String adresse, String codePostal, String ville) throws ImmeubleException {
		try (SelectQueryElement query = new SelectQueryElement(SELECT_LOCALISATION)) {
			query.setArgs(Map.of(
					1, adresse,
					2, ville,
					3, codePostal
			));
			query.execute();
			List<Map<String, Object>> result = query.getResult();
			if (!result.isEmpty()) {
				int idBien = (int) result.getFirst().get("IdImmeuble");
				return new IBuilder(idBien).build();
			}
		} catch (QueryElement.QEltException e) {
			throw new ImmeubleException("Erreur lors de la recherche par localisation", e.getSqlException());
		}
		return null;
	}

	@Override
	public String getVille() {
		return this.ville;
	}

	@Override
	public void setVille(String ville) {
		this.ville = ville;
	}

	@Override
	public String getCodePostal() {
		return this.codePostal;
	}

	@Override
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	@Override
	public String getAdresse() {
		return this.adresse;
	}

	@Override
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.IMMEUBLE;
	}

	public List<Travaux> getTravauxAssocies() {
		//TODO : use a query
		return null;
	}

	public List<BienLouable> getBiensAssocies() throws ImmeubleException {
		List<BienLouable> bienLouablesAssocies = new LinkedList<>();
		try (SelectQueryElement query = new SelectQueryElement(SELECT_BIENS_IMMEUBLES)) {
			query.setArgs(Map.of(1, this.getIdBien()));
			query.execute();
			List<Map<String,Object>> result = query.getResult();
			for(Map<String,Object> row : result) {
				switch(TypeBien.valueOf(row.get("TypeBien").toString())){
					case HABITATION:
						bienLouablesAssocies.add(new Habitation.HBuilder(row).build());
						break;
					case GARAGE:
						bienLouablesAssocies.add(new Garage.GBuilder(row).build());
						break;
					default:
						break;
				}
			}
		} catch (QueryElement.QEltException QEltException) {
			throw new ImmeubleException("Erreur lors de la récupération des biens associés", QEltException.getSqlException());
		}
		return bienLouablesAssocies;
	}

	public static List<Immeuble> findAll() throws ImmeubleException {
		List<Immeuble> immeubles = new LinkedList<>();
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
			selectQueryElement.execute();
			List<Map<String,Object>> result = selectQueryElement.getResult();
			for(Map<String,Object> row : result){
				immeubles.add(new Immeuble.IBuilder(row).build());
			}
		} catch (QueryElement.QEltException qEltException) {
			qEltException.getSqlException().printStackTrace();
			throw new ImmeubleException("Erreur lors de la récupération des immeubles", qEltException.getSqlException());
		}
		return immeubles;
	}

	private Map<String, Object> getArgs() {
		return Map.of(
				"Ville", this.ville,
				"CodePostal", this.codePostal,
				"Adresse", this.adresse,
				"NumeroFiscal", this.getNumeroFiscal(),
				"DateAjout", this.getDateAjout(),
				"IdBien", this.getIdBien(),
				"IdProprio", this.getIdProprio());
	}

	@Override
	public void save() throws ImmeubleException {
		if(this.getIdBien() != -1)
			throw new ImmeubleException("Le bien existe déjà dans la table", null);
		try(UpdateQueryElement q = new UpdateQueryElement(INSERT_QUERY, true)){
			q.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal(),
									4, TypeBien.IMMEUBLE.name(),
									5, this.getNumeroFiscal(),
									6, this.getIdProprio(),
											7, this.getDateAjout()))
					.execute();
			super.save();
			BBuilder.add(this);
		}
		catch (QueryElement.QEltException qEltException){
			throw new ImmeubleException("Erreur lors de l'ajout du bien : " + qEltException.getSqlException().getMessage(), qEltException.getSqlException());
		}
	}

	@Override
	public void modify() throws ImmeubleException {
		if(this.getIdBien() == -1)
			throw new ImmeubleException("Le bien n'existe pas dans la table", null);
		try(UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)){
			query.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal(),
									4, this.getIdProprio(),
									5, this.getIdBien()))
					.execute();
		}catch(QueryElement.QEltException QEltException){
			throw new ImmeubleException("Erreur lors de la modification du bien", QEltException.getSqlException());
		}
	}

	@Override
	public void delete() throws ImmeubleException {
		if(this.getIdBien() == -1)
			throw new ImmeubleException("Le bien n'existe pas dans la table", null);
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)){
			query.setArgs(Map.of(1,this.getIdBien())).execute();
		}
		catch (QueryElement.QEltException e) {
			throw new ImmeubleException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}

	@Override
	public String toString(){
		return this.getIdProprio()+" " + this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}

	public int getNbAppartements() {
		try (SelectQueryElement query = new SelectQueryElement(SELECT_COUNT_BL)) {
			query.setArgs(Map.of(1, this.getIdBien()));
			query.execute();
			List<Map<String,Object>> result = query.getResult();
			if (!result.isEmpty()) {
				return Integer.parseInt(result.getFirst().get("Count(*)").toString());
			}
		} catch (QueryElement.QEltException QEltException) {
			QEltException.printStackTrace();
		}
		return 0;
	}


	public static class IBuilder extends BBuilder{

		private final String adresse;
		private final String ville;
		private final String codePostal;

		IBuilder(String ville, String codePostal, String adresse, String numeroFiscal, java.sql.Date dateAjout, String idProprio, int idBien) {
			super(idBien, numeroFiscal, dateAjout, idProprio);
			this.ville = ville;
			this.codePostal = codePostal;
			this.adresse = adresse;
		}

		IBuilder(Map<String,Object> args) {
			this(args.get("Ville").toString(), args.get("CodePostal").toString(), args.get("Adresse").toString(), args.get("NumeroFiscal").toString(), (Date) args.get("DateAjout"), args.get("IdProprio").toString(), (int) args.get("IdBien"));
		}

		public IBuilder(String ville, String codePostal, String adresse, String numeroFiscal, Date dateAjout,String idProprio) throws BienException {
			this(ville, codePostal, adresse, numeroFiscal,dateAjout, idProprio, -1);
		}

		public IBuilder(int idBien) throws ImmeubleException {
			this(getFromId(idBien));
		}

		private static Map<String,Object> getFromId(int idBien) throws ImmeubleException {
			if(checkNotPresentIn(Immeuble.class,idBien))
				return ((Immeuble) get(idBien)).getArgs();
			try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_FROM_ID)){
				selectQueryElement.setArgs(Map.of(1, idBien));
				selectQueryElement.execute();
				List<Map<String,Object>> result = selectQueryElement.getResult();
				if(result.isEmpty())
					throw new ImmeubleException("L'immeuble n'existe pas dans la base de données", null);
				return selectQueryElement.getResult().getFirst();
			}catch (QueryElement.QEltException qEltException){
				throw new ImmeubleException("Erreur lors de la récupération de l'immeuble, il n'existe peut-être pas dans la base de données", qEltException.getSqlException());
			}
		}

		@Override
		public Immeuble build() {
			if(this.getIdBien() == -1)
				return new Immeuble(this);
			if(checkNotPresentIn(Immeuble.class))
				return (Immeuble) get(this.getIdBien());
			Immeuble immeuble = new Immeuble(this);
			add(immeuble);
			return immeuble;
		}
	}

	public static class ImmeubleException extends BienException {

		public ImmeubleException(String message, SQLException e) {
			super(message, e);
		}
	}
}