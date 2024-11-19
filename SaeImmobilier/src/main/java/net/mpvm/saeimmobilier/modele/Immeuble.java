package net.mpvm.saeimmobilier.modele;
import java.util.ArrayList;
import java.util.List;

public class Immeuble extends Bien{
	private int idImmeuble;
	private List<BienLouable> biensAssocies;
	private List<Travaux> travauxAssocies;

	private Immeuble( String ville, int codePostal, String adresse, int idImmeuble) {
		super( ville, codePostal, adresse); // Initialisation des attributs hérités de Bien
		this.idImmeuble = idImmeuble;
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	public Immeuble(String ville, int codePostal, String adresse) {
		super(ville, codePostal, adresse); // Utilisation du constructeur de Bien
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

	public List<BienLouable> getBiensAssocies() {
		return biensAssocies;
	}

	public void setBiensAssocies(List<BienLouable> biensAssocies) {
		this.biensAssocies = biensAssocies;
	}

	public List<Travaux> getTravauxAssocies() {
		return travauxAssocies;
	}

	public void setTravauxAssocies(List<Travaux> travauxAssocies) {
		this.travauxAssocies = travauxAssocies;
	}
}
