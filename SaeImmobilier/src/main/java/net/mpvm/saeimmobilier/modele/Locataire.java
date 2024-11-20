package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.BD;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Locataire {

	private int numero;
	private String genre;
	private String telepone;
	private String email;
	private String nom;
	private String prenom;
	private ArrayList<Bail> baux;
	private ArrayList<Float> charges;
	private float totalCharge;

	private Locataire(int numero, String nom, String prenom, String email) {
		this.email = email;
		this.numero = numero;
		this.nom = nom;
		this.prenom = prenom;
		this.baux=new ArrayList<>();
		this.charges=new ArrayList<>();
		this.totalCharge=0f;
	}

	public Locataire(String nom, String prenom, String email){
		this(0, nom, prenom, email);
	}

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getGenre() {
		return this.genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
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
		Statement st = Objects.requireNonNull(BD.createStatement());

	}
}
