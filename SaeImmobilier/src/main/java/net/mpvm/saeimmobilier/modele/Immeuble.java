package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public final class Immeuble extends Bien{

	public static final String INSERT_QUERY = "INSERT INTO Bien (Adresse, Ville, CodePostal, TypeBien, NumeroFiscal) VALUES (?, ?, ?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM Bien AND TypeBien = 'IMMEUBLE'";
	public static final String SELECT_WHERE_QUERY = "SELECT * FROM Bien WHERE Adresse = ? AND Ville = ? AND CodePostal = ?";
	public static final String DELETE_QUERY = "DELETE FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String UPDATE_QUERY = "UPDATE Bien SET Adresse = ?, Ville = ?, CodePostal = ?";
	public static final String SELECT_FROM_ID = "SELECT * FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String SELECT_BIENS_IMMEUBLES = "SELECT * FROM Bien WHERE IdBien = ?";

	private static final Map<Integer,Immeuble> immeubles = new HashMap<>();

	private String adresse;
	private String ville;
	private int codePostal;
	private List<Travaux> travauxAssocies;

	private Immeuble(String ville, int codePostal, String adresse, String numeroFiscal, Date dateAjout, int idBien) {
		super(idBien, numeroFiscal, dateAjout); // Initialisation des attributs hérités de Bien
		this.travauxAssocies = new ArrayList<>();
		this.codePostal = codePostal;
		this.adresse = adresse;
		this.ville = ville;
		if((!immeubles.containsKey(this.getIdBien()) || immeubles.get(this.getIdBien()) == null) && idBien != -1){
			immeubles.put(this.getIdBien(),this);
		}
	}

	private Immeuble(IBuilder iBuilder) {
		this(iBuilder.ville, iBuilder.codePostal, iBuilder.adresse, iBuilder.getNumeroFiscal(), iBuilder.getDateAjout(), iBuilder.getIdBien());
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
	public int getCodePostal() {
		return this.codePostal;
	}

	@Override
	public void setCodePostal(int codePostal) {
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

	@Override
	public float getSurface() {
		return 0;
	}

	public List<Travaux> getTravauxAssocies() {
		return travauxAssocies;
	}

	public void setTravauxAssocies(List<Travaux> travauxAssocies) {
		this.travauxAssocies = travauxAssocies;
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
		List<Immeuble> immeubles = new ArrayList<>();
		String query = "SELECT * FROM bien WHERE TypeBien = 'IMMEUBLE'";

		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
			selectQueryElement.execute();
			List<Map<String,Object>> result = selectQueryElement.getResult();
			for(Map<String,Object> row : result){
				// Ajout de l'IdBien s'il est nécessaire dans le constructeur
				immeubles.add(new Immeuble.IBuilder(row).build());
			}
		} catch (QueryElement.QEltException QEltException) {
			throw new ImmeubleException("Erreur lors de la récupération des immeubles", QEltException.getSqlException());
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
				"IdBien", this.getIdBien());
	}


	@Override
	public void save() throws ImmeubleException {
		if(this.getIdBien() != -1)
			throw new ImmeubleException("Le bien existe déjà dans la table");
		try(UpdateQueryElement q = new UpdateQueryElement(INSERT_QUERY, true)){
			q.setArgs(
					Map.of(1, this.getAdresse(),
							2, this.getVille(),
							3, this.getCodePostal(),
							4, TypeBien.IMMEUBLE.name(),
							5, this.getNumeroFiscal()))
					.execute();
			super.save();
			immeubles.put(this.getIdBien(),this);
		}
		catch (QueryElement.QEltException qEltException){
			throw new ImmeubleException("Erreur lors de l'ajout du bien : " + qEltException.getSqlException().getMessage(), qEltException.getSqlException());
		}
	}

	@Override
	public void modify() throws ImmeubleException {
		if(this.getIdBien() == -1)
			throw new ImmeubleException("Le bien n'existe pas dans la table");
		try(UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)){
			query.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal()))
					.execute();
		}catch(QueryElement.QEltException QEltException){
			throw new ImmeubleException("Erreur lors de la modification du bien", QEltException.getSqlException());
		}
	}

	@Override
	public void delete() throws ImmeubleException {
		if(this.getIdBien() == -1)
			throw new ImmeubleException("Le bien n'existe pas dans la table");
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)){
			query.setArgs(Map.of(1,this.getIdBien())).execute();
		}
		catch (QueryElement.QEltException e) {
			throw new ImmeubleException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}

	@Override
	public String toString(){
		return this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}


	public static class IBuilder extends BBuilder{

		private final String adresse;
		private final String ville;
		private final int codePostal;

		IBuilder(String ville, int codePostal, String adresse, String numeroFiscal, Date dateAjout, int idBien) {
			super(idBien, numeroFiscal, dateAjout);
			this.ville = ville;
			this.codePostal = codePostal;
			this.adresse = adresse;
		}

		IBuilder(Map<String,Object> args) {
			this(args.get("Ville").toString(), Integer.parseInt(args.get("CodePostal").toString()), args.get("Adresse").toString(), args.get("NumeroFiscal").toString(), (Date) args.get("DateAjout"), (int) args.get("IdBien"));
		}

		public IBuilder(String ville, int codePostal, String adresse, String numeroFiscal, Date dateAjout) throws BienException {
			this(ville, codePostal, adresse, numeroFiscal,dateAjout,-1);
		}

		public IBuilder(int idBien) throws ImmeubleException {
			this(getFromId(idBien));
		}

		private static Map<String,Object> getFromId(int idBien) throws ImmeubleException {
			if(immeubles.containsKey(idBien))
				return immeubles.get(idBien).getArgs();
			try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_FROM_ID)){
				selectQueryElement.setArgs(Map.of(1, idBien));
				selectQueryElement.execute();
				List<Map<String,Object>> result = selectQueryElement.getResult();
				if(result.isEmpty())
					throw new ImmeubleException("L'immeuble n'existe pas dans la base de données");
				return selectQueryElement.getResult().getFirst();
			}catch (QueryElement.QEltException qEltException){
				throw new ImmeubleException("Erreur lors de la récupération de l'immeuble, il n'existe peut-être pas dans la base de données", qEltException.getSqlException());
			}
		}

		@Override
		public Immeuble build() {
			if(immeubles.containsKey(this.getIdBien()) && immeubles.get(this.getIdBien()) != null)
				return immeubles.get(this.getIdBien());
			return new Immeuble(this);
		}
	}

	public static class ImmeubleException extends BienException {

        public ImmeubleException(String message, SQLException e) {
            super(message, e);
        }
    }
}
