package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import javax.management.ImmutableDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class Immeuble extends Bien{

	public static final String INSERT_QUERY = "INSERT INTO Bien (Adresse, Ville, CodePostal, IdImmeuble, IdProprietaire, TypeBien) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM Bien";
	public static final String SELECT_WHERE_QUERY = "SELECT * FROM Bien WHERE Adresse = ? AND Ville = ? AND CodePostal = ?";
	public static final String DELETE_QUERY = "DELETE FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String UPDATE_QUERY = "UPDATE Bien SET Adresse = ?, Ville = ?, CodePostal = ?";
	public static final String SELECT_FROM_ID = "SELECT * FROM Bien WHERE IdBien = ? AND TypeBien = 'IMMEUBLE'";
	public static final String SELECT_BIENS_IMMEUBLES = "SELECT * FROM Bien WHERE IdBien = ?";
	public static final String SELECT_NEXT_ID = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'bdImmo' AND TABLE_NAME = 'Bien'";

	private String adresse;
	private String ville;
	private int codePostal;
	private List<Travaux> travauxAssocies;

	Immeuble( String ville, int codePostal, String adresse, int idBien) {
		super(idBien); // Initialisation des attributs hérités de Bien
		this.travauxAssocies = new ArrayList<>();
		this.codePostal = codePostal;
		this.adresse = adresse;
		this.ville = ville;
	}

	public Immeuble(String ville, int codePostal, String adresse) {
		this(ville, codePostal, adresse, -1); // Utilisation du constructeur de Bien
	}

	public static Immeuble getFromId(int idImmeuble) throws ImmeubleException {
		try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_FROM_ID)){
			selectQueryElement.setArgs(Map.of(1, idImmeuble));
			ResultSet rs = selectQueryElement.execute();
			return new Immeuble(rs.getString("Ville"), rs.getInt("CodePostal"), rs.getString("Adresse"), rs.getInt("idImmeuble"));
		}catch (QueryElement.QueryException | SQLException e){
			throw new ImmeubleException("Erreur lors de la récupération de l'immeuble", e instanceof SQLException ? (SQLException) e : ((QueryElement.QueryException) e).getSqlException());
		}
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
			ResultSet rs = query.execute();
			while (rs.next()) {
				switch(TypeBien.valueOf(rs.getString("TypeBien"))){
					case HABITATION:
						bienLouablesAssocies.add(new Habitation(rs));
						break;
					case GARAGE:
						bienLouablesAssocies.add(new Garage(rs));
						break;
					default:
						throw new BienException("Les immeubles ne peuvent pas être associés",null);
				}
			}
		} catch (QueryElement.QueryException | SQLException e) {
			throw new ImmeubleException("Erreur lors de la récupération des biens associés", e instanceof SQLException ? (SQLException) e : ((QueryElement.QueryException) e).getSqlException());
		}
		return bienLouablesAssocies;
	}

	public static List<Immeuble> findAll() throws ImmeubleException {
		List<Immeuble> immeubles = new ArrayList<>();
		String query = "SELECT * FROM bien WHERE TypeBien = 'IMMEUBLE'";

		try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {
			selectQueryElement.execute();
			List<Map<String,Object>> rs = selectQueryElement.getResult();
			for(Map<String,Object> row : rs){
				Immeuble immeuble = new Immeuble(
						row.get("Ville").toString(),
						(int) row.get("CodePostal"),
						row.get("Adresse").toString(),
						(int) row.get("IdBien") // Ajout de l'IdBien s'il est nécessaire dans le constructeur
				);
				immeubles.add(immeuble);
			}
		} catch (QueryElement.QueryException queryException) {
			throw new ImmeubleException("Erreur lors de la récupération des immeubles", queryException.getSqlException());
		}
		return immeubles;
	}


	@Override
	public void save() throws ImmeubleException {
		if(this.getIdBien() != -1)
			throw new ImmeubleException("Le bien existe déjà dans la table");
		try(UpdateQueryElement q = new UpdateQueryElement(INSERT_QUERY, true);
		SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_NEXT_ID)){
			ResultSet rs = selectQueryElement.execute();
			rs.next();
			int id = rs.getInt("AUTO_INCREMENT");
			q.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal(),
									4, id,
									5, -1,
									6, TypeBien.IMMEUBLE.name()))
					.execute();
		}
		catch (QueryElement.QueryException | SQLException e){
			throw new ImmeubleException("Erreur lors de l'ajout du bien", e instanceof SQLException ? (SQLException) e : ((QueryElement.QueryException) e).getSqlException());
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
		}catch(QueryElement.QueryException queryException){
			throw new ImmeubleException("Erreur lors de la modification du bien", queryException.getSqlException());
		}
	}

	@Override
	public void delete() throws ImmeubleException {
		if(this.getIdBien() == -1)
			throw new ImmeubleException("Le bien n'existe pas dans la table");
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)){
			query.setArgs(Map.of(1,this.getIdBien())).execute();
		}
		catch (QueryElement.QueryException e) {
			throw new ImmeubleException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}

	@Override
	public String toString(){
		return this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}


	public static class ImmeubleException extends BienException {
		public ImmeubleException(String message) {
			this(message, null);
		}
		public ImmeubleException(String message, SQLException e) {
			super(message, e);
		}
	}

	public static List<Immeuble> findALl() throws Immeuble.ImmeubleException {
		List<Immeuble> p = new ArrayList<>();
		try(SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)){
			ResultSet rs = query.execute();
			while (rs.next()) {
				p.add(
						new Immeuble(
								rs.getString("Ville"),
								Integer.parseInt(rs.getString("CodePostal")),
								rs.getString("Adresse")
						));
			}
		}
		catch (QueryElement.QueryException | SQLException queryException){
			throw new Immeuble.ImmeubleException("Erreur lors de la récupération des immeubles");
		}
		return p;
	}
}
