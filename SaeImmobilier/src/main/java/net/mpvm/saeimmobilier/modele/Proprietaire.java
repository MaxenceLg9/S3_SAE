package net.mpvm.saeimmobilier.modele;
import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.QueryElement.QueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.UpdateQueryElement;

import java.awt.color.ProfileDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
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

	public int getIdProprietaire() {
		return IdProprietaire;
	}

	public void setIdProprietaire(int idProprietaire) {
		IdProprietaire = idProprietaire;
	}

	public ArrayList<Bien> getBiensPossedes() {
		return biensPossedes;
	}

	public void setBiensPossedes(ArrayList<Bien> biensPossedes) {
		this.biensPossedes = biensPossedes;
	}
	public void save() throws ProprietaireException {
		Map<Integer, Object> params = Map.of(1, this.Email, 2, this.MotDePasse);
		try{
			new UpdateQueryElement("INSERT INTO Proprietaire (Email, MotDePasse) VALUES (?, ?)", false).setArgs(params).execute();
		} catch (QueryElement.QueryException e) {
            throw new ProprietaireException("Erreur lors de l'ajout du propriétaire", e);
        }
    }
	public static class ProprietaireException extends Exception {
		public ProprietaireException(String message) {
			super(message);
		}
		public ProprietaireException(String message, Throwable cause) {
			super(message,cause);
		}
	}
}
