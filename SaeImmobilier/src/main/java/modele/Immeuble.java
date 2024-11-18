package modele;

import java.util.ArrayList;
import java.util.List;

public class Immeuble {
	private float iR; // Taux d'intérêt ou autre valeur
	private int idImmeuble;
	private List<Bien> biensAssocies;
	private List<Travaux> travauxAssocies;

	// Constructeur
	public Immeuble(int Immeuble) {
		this.idImmeuble = Immeuble;
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	// Getters et Setters

	public float getiR() {
		return iR;
	}

	public void setiR(float iR) {
		this.iR = iR;
	}

	public int getIdLocation() {
		return idImmeuble;
	}

	public void setIdLocation(int idLocation) {
		this.idImmeuble = idImmeuble;
	}

	public List<Bien> getBiensAssocies() {
		return biensAssocies;
	}

	public void setBiensAssocies(List<Bien> biensAssocies) {
		this.biensAssocies = biensAssocies;
	}




}
