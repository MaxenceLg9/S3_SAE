package modele;

import java.util.ArrayList;
import java.util.List;

public class Immeuble {
	private String adresse;
	private int nouvelIndice;
	private int ancienIndice;
	private float partieFixe;
	private float iR; // Taux d'intérêt ou autre valeur
	private String ville;
	private int codePostal;
	private String lieuImmeuble;
	private int idLocation;
	private String typeBien;
	private List<Bien> biensAssocies;
	private Assurance assurance;
	private List<Travaux> travauxAssocies;

	// Constructeur
	public Immeuble(int idLocation, int codePostal, String adresse) {
		this.idLocation = idLocation;
		this.codePostal = codePostal;
		this.adresse = adresse;
		this.biensAssocies = new ArrayList<>();
	}

	// Getters et Setters
	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getNouvelIndice() {
		return nouvelIndice;
	}

	public void setNouvelIndice(int nouvelIndice) throws IllegalArgumentException {
		if (nouvelIndice < ancienIndice) {
			throw new IllegalArgumentException("Le nouvel indice ne peut pas être inférieur à l'ancien.");
		}
		this.nouvelIndice = nouvelIndice;
	}

	public int getAncienIndice() {
		return ancienIndice;
	}

	public void setAncienIndice(int ancienIndice) {
		this.ancienIndice = ancienIndice;
	}

	public float getPartieFixe() {
		return partieFixe;
	}

	public void setPartieFixe(float partieFixe) {
		this.partieFixe = partieFixe;
	}

	public float getiR() {
		return iR;
	}

	public void setiR(float iR) {
		this.iR = iR;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public int getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	public String getLieuImmeuble() {
		return lieuImmeuble;
	}

	public void setLieuImmeuble(String lieuImmeuble) {
		this.lieuImmeuble = lieuImmeuble;
	}

	public int getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}

	public String getTypeBien() {
		return typeBien;
	}

	public void setTypeBien(String typeBien) throws IllegalArgumentException {
		if (!typeBien.equalsIgnoreCase("bâtiment") &&
				!typeBien.equalsIgnoreCase("logement") &&
				!typeBien.equalsIgnoreCase("garage")) {
			throw new IllegalArgumentException("Le type de bien doit être 'bâtiment', 'logement' ou 'garage'.");
		}
		this.typeBien = typeBien.toLowerCase(); // Normalisation
	}

	public List<Bien> getBiensAssocies() {
		return biensAssocies;
	}

	public void setBiensAssocies(List<Bien> biensAssocies) {
		this.biensAssocies = biensAssocies;
	}

	// Méthodes supplémentaires
	public void associerBien(Bien bien) throws IllegalArgumentException {
		if (typeBien != null && typeBien.equalsIgnoreCase("bâtiment") ) {
			throw new IllegalArgumentException("Les bâtiments ne peuvent pas être associés entre eux.");
		}
		this.biensAssocies.add(bien);
	}

	public Assurance getAssurance() {
		return this.assurance;
	}
	public void setAssurance(Assurance assurance) {
		this.assurance = assurance;
	}

	public void mettreAJourIndice(int nouvelIndice) throws IllegalArgumentException {
		if (nouvelIndice < ancienIndice) {
			throw new IllegalArgumentException("Le nouvel indice ne peut pas être inférieur à l'ancien.");
		}
		this.ancienIndice = this.nouvelIndice;
		this.nouvelIndice = nouvelIndice;
	}
}
