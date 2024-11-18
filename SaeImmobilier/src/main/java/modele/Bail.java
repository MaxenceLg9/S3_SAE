package modele;

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
	private float variationICC; // Ajout dans une classe globale ou spécifique
	private boolean soldeDeToutCompte;
	private boolean colocation;
	private float quotite;


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
	public void revaloriserLoyer(Float nouveauLoyer, Float nouvellesCharges) throws IllegalArgumentException {
		if ((nouveauLoyer == null || nouveauLoyer <= 0) && (nouvellesCharges == null || nouvellesCharges <= 0)) {
			throw new IllegalArgumentException("Le nouveau montant du loyer ou des charges ne doit pas être nul ou négatif.");
		}
		if (nouveauLoyer != null){
			this.montantLoyer = nouveauLoyer;
		}
		if (nouvellesCharges != null){
			this.provisionsCharges = nouvellesCharges;
		}
	}
	public void revaloriserLoyerAvecICC(Float augmentation) throws IllegalArgumentException {
		if (augmentation > variationICC) {
			throw new IllegalArgumentException("L'augmentation dépasse la variation autorisée par l'ICC.");
		}
		this.montantLoyer += augmentation;
	}

	public void regulariserSoldeEtCharges(boolean locatairePart, boolean rendGarage) throws IllegalArgumentException {
		if (!this.soldeDeToutCompte && (locatairePart || rendGarage)) {
			// Calcul et intégration du solde dans la régularisation des charges
			this.soldeDeToutCompte = true; // Marque comme régularisé
		} else if (!locatairePart && !rendGarage) {
			throw new IllegalArgumentException("Le solde ne peut être régularisé que si le locataire part ou rend un garage.");
		}
	}

	public void gererDepartLocataire(Date dateDepart, boolean estColocation) throws IllegalArgumentException {
		if (estColocation) {
			if (this.colocation) {
				this.dateDepart = dateDepart; // Ne termine pas le bail
			} else {
				throw new IllegalArgumentException("Impossible de gérer un départ sans colocation.");
			}
		} else {
			this.dateFinBail = dateDepart; // Fin du bail individuel
		}
	}
	public void gererQuotiteLoyer(boolean estColocation, Float quotite) {
		if (estColocation && quotite != null) {
			this.quotite = quotite;
		} else if (!estColocation && quotite != null) {
			throw new IllegalArgumentException("Quotité de loyer ne peut être renseignée hors colocation.");
		}
	}





}
