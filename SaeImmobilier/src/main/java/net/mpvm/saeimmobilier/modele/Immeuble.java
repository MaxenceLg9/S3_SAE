package modele;

import java.util.ArrayList;
import java.util.List;

public class Immeuble {
	private int idImmeuble;
	private List<Bien> biensAssocies;
	private List<Travaux> travauxAssocies;

	// Constructeur
	public Immeuble(int idImmeuble) {
		this.idImmeuble = idImmeuble;
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	// Getters et Setters

	public int getIdImmeuble() {
		return idImmeuble;
	}

	public void setIdImmeuble(int idImmeuble) {
		this.idImmeuble = idImmeuble;
	}

	public List<Bien> getBiensAssocies() {
		return biensAssocies;
	}

	public void setBiensAssocies(List<Bien> biensAssocies) {
		this.biensAssocies = biensAssocies;
	}

	public List<Travaux> getTravauxAssocies() {
		return travauxAssocies;
	}

	public void setTravauxAssocies(List<Travaux> travauxAssocies) {
		this.travauxAssocies = travauxAssocies;
	}
}
