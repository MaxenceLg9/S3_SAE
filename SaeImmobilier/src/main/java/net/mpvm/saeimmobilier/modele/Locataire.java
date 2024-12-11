package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Locataire implements Queryable {

	public static final String INSERT_QUERY = "INSERT INTO Locataire (nom, prenom, email, sexe, telephone) VALUES (?, ?, ?, ?, ?)";
	public static final String INSERT_QUERY_ARCHIVER = "INSERT INTO ArchiverLocataire (nom, prenom, email, sexe, telephone) VALUES (?, ?, ?, ?, ?)";

	public static final String SELECT_QUERY = "SELECT * FROM Locataire";
	public static final String DELETE_QUERY = "DELETE FROM Locataire WHERE IdLocataire = ?";
	public static final String UPDATE_QUERY = "UPDATE Locataire SET nom = ?, prenom = ?, email = ?, sexe = ?, telephone = ? WHERE IdLocataire = ?";

	private final int IdLocataire;
	private char sexe;
	private String telephone;
	private String email;
	private String nom;
	private String prenom;
	private final ArrayList<Bail> baux;
	private final ArrayList<Float> charges;
	private float totalCharge;


	private Locataire(String nom, String prenom, String email, char sexe, String telephone, int idLocataire) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.sexe = sexe;
		this.telephone = telephone;
		this.IdLocataire = idLocataire;
		this.baux = new ArrayList<>();
		this.charges = new ArrayList<>();
		this.totalCharge = 0f;
	}

	public Locataire(String nom, String prenom, String email, char sexe, String telephone) {
		this(nom, prenom, email, sexe, telephone, -1);
	}

	public int getIdLocataire() {
		return this.IdLocataire;
	}

	public char getSexe() {
		return this.sexe;
	}

	public void setSexe(char sexe) {
		this.sexe = sexe;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public ArrayList<Bail> getBaux(){
		return this.baux;
	}
	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}

	public ArrayList<Float> getCharges(){return this.charges;}

	public void setCharges(float charges) {this.charges.add(charges);}

	public float getTotalCharge(){return this.totalCharge;}

	public void setTotalCharge(float totalCharge) {this.totalCharge = totalCharge;}


	public void save() throws LocataireException{
		if(this.getIdLocataire() != -1)
			throw new LocataireException("Le locataire existe déjà dans la table");
		try(UpdateQueryElement query =new UpdateQueryElement(INSERT_QUERY, true)){
					query.setArgs(
							Map.of(1, this.getNom(),
									2, this.getPrenom(),
									3, this.getEmail(),
									4, Character.toString(this.getSexe()),
									5, this.getTelephone()))
					.execute();
        }
		catch (QueryElement.QueryException sqlE){
			throw new LocataireException("Erreur lors de l'ajout du locataire",sqlE);
		}

	}

	public void delete() throws LocataireException {
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)){
			query.setArgs(Map.of(1,this.getIdLocataire())).execute();
        }
		catch (QueryElement.QueryException e) {
			throw new LocataireException("Erreur lors de la suppression du locataire");
		}
	}

	public void modify() throws LocataireException{
		if(this.getIdLocataire() == -1)
			throw new LocataireException("Vous ne pouvez pas modifier un locataire qui n'existe pas");
		try(UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)){
			query.setArgs(
					Map.of(1, this.getNom(),
									2, this.getPrenom(),
									3, this.getEmail(),
									4, Character.toString(this.getSexe()),
									5, this.getTelephone(),
									6, this.getIdLocataire())).execute();
		}catch(QueryElement.QueryException e){
			throw new LocataireException("Erreur lors de la modification du locataire");
		}
	}

	public String toString(){
		return "Nom : " + this.getNom() + ", Prenom : " + this.getPrenom() + ", Email : " + this.getEmail();
	}

	public static List<Locataire> findALl() throws LocataireException {
		List<Locataire> l = new ArrayList<>();

		try(SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)){
			ResultSet rs = query.execute();
			while (rs.next()) {
				l.add(
						new Locataire(rs.getString("nom"),
								rs.getString("prenom"),
								rs.getString("email"),
								rs.getString("sexe").charAt(0),
								rs.getString("telephone"),
								rs.getInt("IdLocataire")));
			}
		}
		catch (QueryElement.QueryException | SQLException queryException){
			throw new LocataireException("Erreur lors de la récupération des locataires");
		}
		return l;
	}

	public static class LocataireException extends QueryableException{
		public LocataireException(String message){
			super(message);
		}
		public LocataireException(String message, Throwable cause){
			super(message,cause);
		}
	}
}

