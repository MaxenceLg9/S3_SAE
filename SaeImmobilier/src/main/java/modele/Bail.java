package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bail {
	private int nbMoisLoues;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	private ArrayList<Bien> biens;
	private ArrayList<Locataire> locataires;

	// Répartitions par locataire
	private Map<Locataire, Float> repartitionElectricite;
	private Map<Locataire, Float> repartitionOrduresMenageres;
	private Map<Locataire, Float> repartitionEntretien;

	public Bail(Date dateDebut) {
		this.dateDebut = dateDebut;
		this.biens = new ArrayList<>();
		this.locataires = new ArrayList<>();
		this.repartitionElectricite = new HashMap<>();
		this.repartitionOrduresMenageres = new HashMap<>();
		this.repartitionEntretien = new HashMap<>();
	}

	// Getters et setters pour les répartitions
	public Map<Locataire, Float> getRepartitionElectricite() {
		return this.repartitionElectricite;
	}

	public void setRepartitionElectricite(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionElectricite.put(locataire, pourcentage);
	}

	public Map<Locataire, Float> getRepartitionOrduresMenageres() {
		return this.repartitionOrduresMenageres;
	}

	public void setRepartitionOrduresMenageres(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionOrduresMenageres.put(locataire, pourcentage);
	}

	public Map<Locataire, Float> getRepartitionEntretien() {
		return this.repartitionEntretien;
	}

	public void setRepartitionEntretien(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionEntretien.put(locataire, pourcentage);
	}

	// Autres getters et setters
	public ArrayList<Bien> getLogements() {
		return this.biens;
	}

	public void ajouterLogement(Bien bien) {
		this.biens.add(bien);
	}

	public ArrayList<Locataire> getLocataires() {
		return this.locataires;
	}

	public void ajouterLocataire(Locataire locataire) {
		this.locataires.add(locataire);
	}

	public Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		if (this.dateDebut == null || this.nbMoisLoues <= 0) {
			return null;
		}

		// Utilisation de la méthode addMonths pour calculer la date de fin
		return this.dateDebut.addMonths(this.nbMoisLoues);
	}


	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public int getNbMoisLoues() {
		return nbMoisLoues;
	}

	public void setNbMoisLoues(int nbMoisLoues) {
		this.nbMoisLoues = nbMoisLoues;
	}
}
