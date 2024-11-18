package modele;

import java.util.ArrayList;

public class Bien {
	private String lieuImmeuble;
	private int idLocation;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private int surface;
	private String numeroFiscal;
	private Immeuble immeuble;
	private Proprietaire proprietaire;

	public Bien(int idLocation, String lieu, Immeuble immeuble) {
		this.idLocation = idLocation;
		this.lieuImmeuble = lieu;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
		this.immeuble = immeuble;
	}

	// Getters and setters (inchangés, sans les répartitions)
	public String getLieuImmeuble() {
		return this.lieuImmeuble;
	}

	public int getIdLocation() {
		return this.idLocation;
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

	public void ajouterTravail(Travaux travail) {
		this.travaux.add(travail);
	}

	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}
}
