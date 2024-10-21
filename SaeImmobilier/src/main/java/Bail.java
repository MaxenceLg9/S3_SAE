
public class Bail {
	private int nbMoisLoues;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularitationCharge;
	private Date date;

	public Bail(Date date) {
		this.date = date;
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
