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

	// Constructeur
	public Bail(Date dateDebut) {
		this.dateDebut = dateDebut;
		this.biens = new ArrayList<>();
		this.locataires = new ArrayList<>();
		this.repartitionElectricite = new HashMap<>();
		this.repartitionOrduresMenageres = new HashMap<>();
		this.repartitionEntretien = new HashMap<>();
	}

	// Méthode pour savoir si le bail est en colocation
	public boolean estEnColocation() {
		return this.locataires.size() > 1;
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

	public float getLoyer() {
		return loyer;
	}

	public void setLoyer(float loyer) {
		this.loyer = loyer;
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

	public float getRegularisationCharge() {
		return regularisationCharge;
	}

	public void setRegularisationCharge(float regularisationCharge) {
		this.regularisationCharge = regularisationCharge;
	}
}
