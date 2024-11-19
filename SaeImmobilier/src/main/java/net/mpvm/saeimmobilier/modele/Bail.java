package net.mpvm.saeimmobilier.modele;

import java.util.ArrayList;

public class Bail {
	private int nbMoisLoues;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	private ArrayList<Logement> logements;
	private ArrayList<Locataire> locataires;

	public Bail(Date dateDebut,Integer Annee, Integer Mois, Integer Jour) {
        this.dateDebut = new Date(Annee, Mois, Jour);
		this.logements=new ArrayList<>();
		this.locataires=new ArrayList<>();
	}

	public int getNbMoisLoues() {
		return this.nbMoisLoues;
	}
	public ArrayList<Logement> getLogements() {
		return this.logements;
	}
	public ArrayList<Locataire> getLocataires() {
		return this.locataires;
	}
	public void ajouterLocataire(Locataire locataire) {
		this.locataires.add(locataire);
	}
	public void ajouterLogement(Logement logement) {
		this.logements.add(logement);
	}
	public void setNbMoisLoues(int nbMoisLoues) {
		this.nbMoisLoues = nbMoisLoues;
	}

	public float getProvisionSurCharge() {
		return this.provisionSurCharge;
	}

	public void setProvisionSurCharge(float provisionSurCharge) {
		this.provisionSurCharge = provisionSurCharge;
	}

	public float getFactureEau() {
		return this.factureEau;
	}

	public void setFactureEau(float factureEau) {
		this.factureEau = factureEau;
	}

	public float getTotalCharge() {
		return this.totalCharge;
	}

	public void setTotalCharge(float totalCharge) {
		this.totalCharge = totalCharge;
	}

	public float getLoyer() {
		return this.loyer;
	}

	public void setLoyer(float loyer) {
		this.loyer = loyer;
	}

	public float getRegularitationCharge() {
		return this.regularisationCharge;
	}

	public void setRegularitationCharge(float regularitationCharge) {
		this.regularisationCharge = regularitationCharge;
	}

	public Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Date getDateFin() {
		while (!(this.getNbMoisLoues()+this.dateDebut.getMois()<=12)) {
			this.dateFin.setAnnee(this.dateFin.getAnnee()+1);
		}
		this.dateFin=this.dateDebut.setMois(this.dateDebut.getMois()+this.getNbMoisLoues());
		return this.dateFin;
		
	}
	
}
