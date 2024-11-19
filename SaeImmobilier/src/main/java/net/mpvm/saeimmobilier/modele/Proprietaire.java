package net.mpvm.saeimmobilier.modele;



import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proprietaire {
	private String Nom;
	private String Prenom;
	private String Telephone;
	private String Email;
	private String MotDePasse;
	private String Ville;
	private Integer CodePostal;
	private String Adresse;
	private ArrayList<Bien> biensPossedes;
	private int IdProprietaire;
	private Proprietaire(int IdProprietaire,String Email,String MotDePasse) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.Email = Email;
		this.IdProprietaire = IdProprietaire;
		this.MotDePasse = MotDePasse;
		this.biensPossedes = new ArrayList<>();
	}
	public Proprietaire(String Email, String MotDePasse) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.Email = Email;
		this.MotDePasse = MotDePasse;
		this.biensPossedes = new ArrayList<>();
	}

	// Getters et setters pour les propriétés

	public String getAdresse() {
		return this.Adresse;
	}

	public Integer getCodePostal() {
		return this.CodePostal;
	}

	public String getEmail() {
		return this.Email;
	}

	public String getMotDePasse() {
		return this.MotDePasse;
	}

	public String getNom() {
		return this.Nom;
	}

	public String getPrenom() {
		return this.Prenom;
	}

	public String getTelephone() {
		return this.Telephone;
	}

	public String getVille() {
		return this.Ville;
	}

	public void setAdresse(String adresse) {
		this.Adresse = adresse;
	}

	public void setCodePostal(Integer codePostal) {
		this.CodePostal = codePostal;
	}

	public void setEmail(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		this.Email = email;
	}

	public void setMotDePasse(String motDePasse) {
		if (motDePasse.length() < 8 || motDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.MotDePasse = motDePasse;
	}

	public void setNom(String nom) {
		this.Nom = nom;
	}

	public void setPrenom(String prenom) {
		this.Prenom = prenom;
	}

	public void setTelephone(String telephone) {
		this.Telephone = telephone;
	}

	public void setVille(String ville) {
		this.Ville = ville;
	}

	public boolean verifierMontantRegularisation(float sommeVersee, float sommeDue) {
		return Math.abs(sommeVersee - sommeDue) < 0.01; // Tolérance pour arrondis
	}
}
