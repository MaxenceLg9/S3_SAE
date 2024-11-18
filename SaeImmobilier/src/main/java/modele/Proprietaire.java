package modele;

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
	private Bien[] biensLoues;

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

	// Méthodes pour la gestion des charges

	public float calculerRegularisationCharges() {
		float totalCharges = 0;

		if (biensLoues != null) {
			for (Bien logement : biensLoues) {
				for (Bail bail : logement.getBaux()) {
					// Accumule les répartitions des locataires associées au bail
					for (Float part : bail.getRepartitionElectricite().values()) {
						totalCharges += part;
					}
					for (Float part : bail.getRepartitionEntretien().values()) {
						totalCharges += part;
					}
					for (Float part : bail.getRepartitionOrduresMenageres().values()) {
						totalCharges += part;
					}
				}
			}
		}
		return totalCharges;
	}

	public boolean verifierMontantRegularisation(float sommeVersee, float sommeDue) {
		return Math.abs(sommeVersee - sommeDue) < 0.01; // Tolérance pour arrondis
	}
}
