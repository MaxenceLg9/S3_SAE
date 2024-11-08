package modele;
public class Logement {
	private String lieuImmeuble;
	private float repartitionElectricite;
	private float repartitionOrduresMenageres;
	private float repartitionEntretien;
	private int idLocation;
	private List<Travaux> travaux;
	private List<Bail> baux;
	
	public Logement(int idLocation, String lieu) {
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
	public Travaux[] getTravaux() {
		return this.travaux;
	}
	public Bail[] getBaux() {
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
}
