package net.mpvm.saeimmobilier.modele;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Immeuble extends Bien{

	public static final String INSERT_QUERY = "INSERT INTO immeuble (Adresse, Ville, CodePostal) VALUES (?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM immeuble";
	public static final String SELECT_WHERE_QUERY = "SELECT * FROM immeuble WHERE Adresse = ? AND Ville = ? AND CodePostal = ?";
	public static final String DELETE_QUERY = "DELETE FROM immeuble WHERE idImmeuble = ?";
	public static final String UPDATE_QUERY = "UPDATE immeuble SET Adresse = ?, Ville = ?, CodePostal = ?";
	public static final String SELECT_FROM_ID = "SELECT * FROM immeuble WHERE idImmeuble = ?";
	public static final String SELECT_BIENS_IMMEUBLES = "SELECT * FROM immeuble WHERE idImmeuble = ?";


	private List<Travaux> travauxAssocies;

	private Immeuble( String ville, int codePostal, String adresse, int idImmeuble) {
		super(ville, codePostal, adresse, idImmeuble); // Initialisation des attributs hérités de Bien
		this.travauxAssocies = new ArrayList<>();
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
	@Override
	public String toString(){
		return this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}


	@Override
	public void save() throws ImmeubleException {
		if(this.getIdBien() != -1)
			throw new ImmeubleException("Le bien existe déjà dans la table");
		try(UpdateQueryElement q = new UpdateQueryElement(INSERT_QUERY, true)){
			q.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal()))
					.execute();
		}
		catch (QueryElement.QueryException queryException){
			throw new ImmeubleException("Erreur lors de l'ajout du bien", queryException.getSqlException());
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

	public static class ImmeubleException extends BienException {
		public ImmeubleException(String message) {
			this(message, null);
		}
		public ImmeubleException(String message, SQLException e) {
			super(message, e);
		}
	}
}
