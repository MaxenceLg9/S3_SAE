package net.mpvm.saeimmobilier.modele;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class Bail {
	private int idBail;
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
	private ArrayList<Charges> charges;
	private Map<Locataire, Float> repartitionElectricite;
	private Map<Locataire, Float> repartitionOrduresMenageres;
	private Map<Locataire, Float> repartitionEntretien;
	private boolean colocation;

	// Constructeur
	public Bail(Date dateDebut) {
		this.dateDebut = dateDebut;
		this.biens = new ArrayList<>();
		this.locataires = new ArrayList<>();
		this.repartitionElectricite = new HashMap<>();
		this.repartitionOrduresMenageres = new HashMap<>();
		this.repartitionEntretien = new HashMap<>();
		this.charges = new ArrayList<>();
	}

	// Méthode pour savoir si le bail est en colocation
	public boolean estEnColocation() {
		if (this.locataires.size() > 1) {
			this.colocation = true;
		}
		return this.colocation;
	}

	// Méthode pour diviser le loyer entre colocataires
	public Map<Locataire, Float> diviserLoyer() {
		if (this.locataires.isEmpty()) {
			throw new IllegalStateException("Aucun locataire n'est associé au bail.");
		}

		Map<Locataire, Float> partsLoyer = new HashMap<>();
		if (estEnColocation()) {
			float totalPourcentage = 0;
			boolean utilisationRepartition = false;

			// Vérifier si des répartitions sont définies
			for (Locataire locataire : locataires) {
				if (repartitionElectricite.containsKey(locataire) ||
						repartitionEntretien.containsKey(locataire) ||
						repartitionOrduresMenageres.containsKey(locataire)) {
					utilisationRepartition = true;
					totalPourcentage += repartitionElectricite.getOrDefault(locataire, 0f);
				}
			}

			if (utilisationRepartition && totalPourcentage > 0) {
				// Répartition en fonction des pourcentages définis
				for (Locataire locataire : locataires) {
					float pourcentage = repartitionElectricite.getOrDefault(locataire, 0f);
					partsLoyer.put(locataire, this.loyer * pourcentage);
				}
			} else {
				// Répartition équitable
				float partEquitable = this.loyer / this.locataires.size();
				for (Locataire locataire : locataires) {
					partsLoyer.put(locataire, partEquitable);
				}
			}
		} else {
			// Bail sans colocation : un seul locataire paie l'intégralité
			partsLoyer.put(locataires.get(0), this.loyer);
		}
		return partsLoyer;
	}

	// Méthode pour ajouter un logement
	public void ajouterLogement(Bien bien) {
		this.biens.add(bien);
	}

	// Méthode pour ajouter un locataire
	public void ajouterLocataire(Locataire locataire) {
		this.locataires.add(locataire);
	}

	// Méthode pour obtenir la date de fin en fonction de la durée
	public Date calculerDateFin() {
		if (this.dateDebut == null || this.nbMoisLoues <= 0) {
			return null;
		}

		// Utilisation d'une méthode fictive `addMonths` pour calculer la date
		return this.dateDebut.addMonths(this.nbMoisLoues);
	}

	// Getters et Setters

	public int getIdBail() {
		return idBail;
	}

	public void setIdBail(int idBail) {
		this.idBail = idBail;
	}

	public int getNbMoisLoues() {
		return nbMoisLoues;
	}

	public void setNbMoisLoues(int nbMoisLoues) {
		this.nbMoisLoues = nbMoisLoues;
	}

	public float getProvisionSurCharge() {
		return provisionSurCharge;
	}

	public void setProvisionSurCharge(float provisionSurCharge) {
		this.provisionSurCharge = provisionSurCharge;
	}

	public float getFactureEau() {
		return factureEau;
	}

	public void setFactureEau(float factureEau) {
		this.factureEau = factureEau;
	}

	public float getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(float totalCharge) {
		this.totalCharge = totalCharge;
	}

	public float getLoyer() {
		return loyer;
	}

	public void setLoyer(float loyer) {
		if (loyer <= 0) {
			throw new IllegalArgumentException("Le loyer doit être positif.");
		}
		this.loyer = loyer;
	}

	public float getRegularisationCharge() {
		return regularisationCharge;
	}

	public void setRegularisationCharge(float regularisationCharge) {
		this.regularisationCharge = regularisationCharge;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public ArrayList<Bien> getBiens() {
		return biens;
	}

	public ArrayList<Locataire> getLocataires() {
		return locataires;
	}

	public ArrayList<Charges> getCharges() {
		return charges;
	}

	public Map<Locataire, Float> getRepartitionElectricite() {
		return repartitionElectricite;
	}

	public void setRepartitionElectricite(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionElectricite.put(locataire, pourcentage);
	}

	public Map<Locataire, Float> getRepartitionOrduresMenageres() {
		return repartitionOrduresMenageres;
	}

	public void setRepartitionOrduresMenageres(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionOrduresMenageres.put(locataire, pourcentage);
	}

	public Map<Locataire, Float> getRepartitionEntretien() {
		return repartitionEntretien;
	}

	public void setRepartitionEntretien(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		this.repartitionEntretien.put(locataire, pourcentage);
	}
}
