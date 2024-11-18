package modele;

import java.util.ArrayList;

public class Logement {
	private String lieuImmeuble;
	private float repartitionElectricite;
	private float repartitionOrduresMenageres;
	private float repartitionEntretien;
	private int idLocation;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private int surface;
	private String numeroFiscal;

	public Propriété(int idLocation, String lieu) {
		this.idLocation=idLocation;
		this.lieuImmeuble=lieu;
		this.travaux=new ArrayList<>();
		this.baux=new ArrayList<>();
	}
	
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
	public float getRepartitionElectricite() {
		return this.repartitionElectricite;
	}
	public float getRepartitionEntretien() {
		return this.repartitionEntretien;
	}
	public float getRepartitionOrduresMenageres() {
		return this.repartitionOrduresMenageres;
	}
	
	public void setIdLocation(int idLocation) {
		this.idLocation=idLocation;
	}
	public void ajouterTravail(Travaux travail) {
		this.travaux.add(travail);
	}
	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}
	public void setRepartitionElectricite(float pourcentage) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionElectricite=pourcentage;
	}
	public void setRepartitionOrduresMenageres(float pourcentage) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionOrduresMenageres=pourcentage;
	}
	public void setRepartitionEntretien(float pourcentage) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionEntretien=pourcentage;
	}
	public void setLieuImmeuble(String lieu) {
		this.lieuImmeuble=lieu;
	}
	public void verifierCohérenceCharges(Float taxeOrdures) throws IllegalArgumentException {
		if (this.repartitionElectricite + this.repartitionEntretien + this.repartitionOrduresMenageres <= taxeOrdures) {
			throw new IllegalArgumentException("Le montant total des charges doit être strictement supérieur à la taxe des ordures ménagères.");
		}
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
	public void setSurface(Integer surface) {
		if (surface == null || surface <= 0) {
			throw new IllegalArgumentException("La surface doit être renseignée et positive.");
		}
		this.surface = surface;
	}
	public void setNumeroFiscal(String numeroFiscal) {
		if (this instanceof Logement || this instanceof Garage) {
			if (numeroFiscal == null || numeroFiscal.isEmpty()) {
				throw new IllegalArgumentException("Un numéro fiscal est obligatoire pour ce type de bien.");
			}
		}
		this.numeroFiscal = numeroFiscal;
	}
}
