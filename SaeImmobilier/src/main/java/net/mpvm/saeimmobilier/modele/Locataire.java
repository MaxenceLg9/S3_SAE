package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.BD;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Locataire {

	private int numero;
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

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
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
		String insertLocataire = "INSERT INTO Locataires (nom, prenom, email, sexe, telephone) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement st = Objects.requireNonNull(BD.prepareStatement(insertLocataire));
        try {
			st.setString(1, this.nom);
			st.setString(2, this.prenom);
			st.setString(3,this.email);
			st.setString(4, Character.toString(this.sexe));
			st.setString(5, this.telepone);
			int row = st.executeUpdate();
			System.out.println(row + " rows affected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
