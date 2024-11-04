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
	private Float Electricite;
	private Float OrduresMenageres;
	private Float Entretien;

	public Proprietaire(String Email, String MotDePasse) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		boolean matchFound = matcher.matches();
		if (!matchFound) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.Email = Email;
		this.MotDePasse = MotDePasse;

	}

	public String getAdresse() {
		return this.Adresse;
	}

	public Integer getCodePostal() {
		return this.CodePostal;
	}

	public Float getElectricite() {
		return this.Electricite;
	}

	public String getEmail() {
		return this.Email;
	}

	public Float getEntretien() {
		return this.Entretien;
	}

	public String getMotDePasse() {
		return this.MotDePasse;
	}

	public String getNom() {
		return this.Nom;
	}

	public Float getOrduresMenageres() {
		return this.OrduresMenageres;
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

	public void setElectricite(Float electricite) {
		this.Electricite = electricite;
	}

	public void setEmail(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(this.Email);
		boolean matchFound = matcher.matches();
		if (!matchFound) {
			throw new IllegalArgumentException("Email non valide");
		}
		this.Email = email;
	}

	public void setEntretien(Float entretien) {
		this.Entretien = entretien;
	}

	public void setMotDePasse(String motDePasse) {
		if (this.MotDePasse.length() < 8 || this.MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.MotDePasse = motDePasse;
	}

	public void setNom(String nom) {
		this.Nom = nom;
	}

	public void setOrduresMenageres(Float orduresMenageres) {
		this.OrduresMenageres = orduresMenageres;
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

}
