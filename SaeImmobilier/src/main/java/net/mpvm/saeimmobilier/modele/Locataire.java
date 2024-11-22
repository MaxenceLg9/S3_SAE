package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.BD;
import net.mpvm.saeimmobilier.sql.QueryElement;
import net.mpvm.saeimmobilier.sql.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.UpdateQueryElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locataire {

	public static final String INSERT_QUERY = "INSERT INTO Locataire (nom, prenom, email, sexe, telephone) VALUES (?, ?, ?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM Locataire";
	private static final String DELETE_QUERY = "DELETE FROM Locataire WHERE IdLocataire = ?";

	private int id;
	private char sexe;
	private String telepone;
	private String email;
	private String nom;
	private String prenom;
	private final ArrayList<Bail> baux;
	private final ArrayList<Float> charges;
	private float totalCharge;

	private Locataire(String nom, String prenom, String email) {
		this.email = email;
		this.nom = nom;
		this.prenom = prenom;
		this.baux=new ArrayList<>();
		this.charges=new ArrayList<>();
		this.totalCharge=0f;
	}

	public Locataire(String nom, String prenom, String email, char sexe, String telepone) {
		this(nom, prenom, email);
		this.sexe = sexe;
		this.telepone = telepone;
	}

	public int getId() {
		return this.id;
	}

	private Locataire setId(int id) {
		this.id = id;
		return this;
	}

	public char getSexe() {
		return this.sexe;
	}

	public void setSexe(char sexe) {
		this.sexe = sexe;
	}

	public String getTelepone() {
		return this.telepone;
	}

	public void setTelepone(String telepone) {
		this.telepone = telepone;
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

	public void save() {

		try{
			new UpdateQueryElement(INSERT_QUERY).addArgs(Map.of(1, this.nom, 2, this.prenom, 3, this.email, 4, Character.toString(this.sexe), 5, this.telepone)).execute();
		}
		catch (SQLException sqlE){
			sqlE.printStackTrace();
		}
	}

	public void delete(){
		try {
			new UpdateQueryElement(DELETE_QUERY).addArgs(Map.of(1,this.id)).execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Locataire> getLocataires() {
		List<Locataire> l = new ArrayList<>();
		try(ResultSet rs = new SelectQueryElement(SELECT_QUERY).execute()) {
			while (rs.next()) {
				l.add(new Locataire(rs.getString("nom"),rs.getString("prenom"),rs.getString("email"),rs.getString("sexe").charAt(0),rs.getString("telephone")).setId(rs.getInt("IdLocataire")));
			}
		}
		catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
		return l;
	}
}