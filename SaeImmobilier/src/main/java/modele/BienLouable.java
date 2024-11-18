package modele;

import java.util.ArrayList;

public class BienLouable {
	private String lieuImmeuble;

	private int idBienLouable;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private int surface;
	private String numeroFiscal;
	private Immeuble immeuble;
	private Proprietaire proprietaire;

	public BienLouable(int idLocation, String lieu, Immeuble immeuble) {
		this.idBienLouable = idLocation;
		this.lieuImmeuble = lieu;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
		this.immeuble = immeuble;
	}

	// Getters
	public String getLieuImmeuble() {
		return this.lieuImmeuble;
	}

	public int getIdLocation() {
		return this.idBienLouable;
	}

	public ArrayList<Travaux> getTravaux() {
		return this.travaux;
	}

	public ArrayList<Bail> getBaux() {
		return this.baux;
	}

	public int getAncienIndex() {
		return this.ancienIndex;
	}

	public boolean isChangementCompteur() {
		return this.changementCompteur;
	}

	public int getSurface() {
		return this.surface;
	}

	public String getNumeroFiscal() {
		return this.numeroFiscal;
	}

	public Immeuble getImmeuble() {
		return this.immeuble;
	}

	public Proprietaire getProprietaire() {
		return this.proprietaire;
	}

	// Setters
	public void setIdLocation(int idLocation) {
		this.idBienLouable = idLocation;
	}

	public void setLieuImmeuble(String lieu) {
		this.lieuImmeuble = lieu;
	}


	public void setAncienIndex(int ancienIndex) {
		this.ancienIndex = ancienIndex;
	}

	public void setChangementCompteur(boolean changementCompteur) {
		this.changementCompteur = changementCompteur;
	}

	public void setSurface(int surface) {
		if (surface <= 0) {
			throw new IllegalArgumentException("La surface doit être positive.");
		}
		this.surface = surface;
	}

	public void setNumeroFiscal(String numeroFiscal) {
		if (numeroFiscal == null || numeroFiscal.isEmpty()) {
			throw new IllegalArgumentException("Un numéro fiscal est obligatoire pour ce type de bien.");
		}
		if (numeroFiscal.length() != 12) {
			throw new IllegalArgumentException("Numéro Fiscal différent de 12 caractères");
		}
		this.numeroFiscal = numeroFiscal;
	}

	public void setImmeuble(Immeuble immeuble) {
		if (immeuble == null) {
			throw new IllegalArgumentException("L'immeuble associé ne peut pas être null.");
		}
		this.immeuble = immeuble;
	}

	public void setProprietaire(Proprietaire proprietaire) {
		if (proprietaire == null) {
			throw new IllegalArgumentException("Un bien doit avoir un propriétaire.");
		}
		this.proprietaire = proprietaire;
	}

	// Additional Methods
	public void ajouterTravail(Travaux travail) {
		this.travaux.add(travail);
	}

	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}



	public void mettreAJourIndexCompteur(int nouvelIndex, boolean compteurChange) throws IllegalArgumentException {
		if (compteurChange) {
			this.changementCompteur = true;
			this.ancienIndex = nouvelIndex; // Nouveau compteur
		} else {
			if (nouvelIndex < this.ancienIndex) {
				throw new IllegalArgumentException("Le nouvel index ne peut pas être inférieur à l'ancien.");
			}
			this.ancienIndex = nouvelIndex;
		}
	}
}
