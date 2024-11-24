package net.mpvm.saeimmobilier.modele;


import java.util.ArrayList;

public class BienLouable extends Bien {
	private int idBienLouable;
	private String lieuImmeuble;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private float surface;
	private String numeroFiscal;
	private Immeuble immeuble;
	private Proprietaire proprietaire;
	private int nbPieces;
	private int codePostal;

	private BienLouable( String ville, int codePostal, String adresse,
						 int idBienLouable,int nbPieces, int NumeroFiscal, Immeuble immeuble, float surface) {
		super( ville, codePostal, adresse); // Initialisation des attributs hérités de Bien
		this.idBienLouable = idBienLouable;
		this.lieuImmeuble = lieuImmeuble;
		this.immeuble = immeuble;
		this.surface = surface;
		this.nbPieces = nbPieces;
		this.codePostal = codePostal;
		this.numeroFiscal = numeroFiscal;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
	}

	public BienLouable(String ville, int codePostal, String adresse,int nbPieces, int NumeroFiscal,float surface, Immeuble immeuble) {
		super(ville, codePostal, adresse); // Utilisation du constructeur de Bien pour initier ville, codePostal, adresse
		this.lieuImmeuble = lieuImmeuble;
		this.immeuble = immeuble;
		this.surface = surface;
		this.nbPieces = nbPieces;
		this.codePostal = codePostal;
		this.numeroFiscal = numeroFiscal;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
	}




	// Getters et Setters pour tous les champs

	public int getIdBienLouable() {
		return idBienLouable;
	}

	public void setIdBienLouable(int idBienLouable) {
		this.idBienLouable = idBienLouable;
	}

	public String getLieuImmeuble() {
		return lieuImmeuble;
	}

	public void setLieuImmeuble(String lieuImmeuble) {
		this.lieuImmeuble = lieuImmeuble;
	}

	public ArrayList<Travaux> getTravaux() {
		return travaux;
	}

	public void ajouterTravaux(Travaux travail) {
		this.travaux.add(travail);
	}

	public ArrayList<Bail> getBaux() {
		return baux;
	}

	@Override
	public int getCodePostal() {
		return codePostal;
	}

	@Override
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}

	public int getAncienIndex() {
		return ancienIndex;
	}

	public void setAncienIndex(int ancienIndex) {
		this.ancienIndex = ancienIndex;
	}

	public boolean isChangementCompteur() {
		return changementCompteur;
	}

	public void setChangementCompteur(boolean changementCompteur) {
		this.changementCompteur = changementCompteur;
	}

	public void setSurface(float surface) {
		this.surface = surface;
	}

	public float getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		if (surface <= 0) {
			throw new IllegalArgumentException("La surface doit être positive.");
		}
		this.surface = surface;
	}

	public String getNumeroFiscal() {
		return numeroFiscal;
	}

	public void setNumeroFiscal(String numeroFiscal) {
		if (numeroFiscal == null || numeroFiscal.length() != 12) {
			throw new IllegalArgumentException("Le numéro fiscal doit être de 12 caractères.");
		}
		this.numeroFiscal = numeroFiscal;
	}

	public Immeuble getImmeuble() {
		return immeuble;
	}

	public void setImmeuble(Immeuble immeuble) {
		this.immeuble = immeuble;
	}

	public Proprietaire getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Proprietaire proprietaire) {
		this.proprietaire = proprietaire;
	}

	public void setTravaux(ArrayList<Travaux> travaux) {
		this.travaux = travaux;
	}

	public void setBaux(ArrayList<Bail> baux) {
		this.baux = baux;
	}
	public int getNbPieces() {
		return nbPieces;
	}
	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}
}
