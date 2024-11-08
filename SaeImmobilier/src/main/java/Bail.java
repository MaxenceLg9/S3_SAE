package modele;

public class Bail extends Date{
	private int nbMoisLoues;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	

	public Bail(Date dateDebut) {
		this.date = dateDebut;
	}

	public int getNbMoisLoues() {
		return this.nbMoisLoues;
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
		return this.regularitationCharge;
	}

	public void setRegularitationCharge(float regularitationCharge) {
		this.regularitationCharge = regularitationCharge;
	}

	public Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Date getDateFin() {
		int nbMois=this.nbMoisLoues;
		int anneeARajouter=0;
		while (!this.getNbMoisLoues()+this.dateDebut.getMois()<=12) {
			int nbMois=nbMois-12;
			int anneeARajouter=+1;
		}	
		
		this.dateFin=this.dateDebut.setMois(this.dateDebut.getMois()+this.getNbMoisLoues());
		return this.dateFin;
		
	}
	
}
