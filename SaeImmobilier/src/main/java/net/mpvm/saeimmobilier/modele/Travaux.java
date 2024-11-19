package net.mpvm.saeimmobilier.modele;

public class Travaux {
	private String NumeroFacture;
	private String Entreprise;
	private Float Montant;
	private Float MontantNonDeductible;
	private Float Reduction;
	private Date Date;
	private String Nature;
	private String NumeroDevis;
	private Float MontantADeclarer;
	private String entrepriseDevis;
	private Float montantDevis;
	private Immeuble immeuble;
	private Bien bien;


	public Travaux(String NumeroDeFacture, String Entreprise, Date Date) {
		this.Date = Date;
		this.NumeroFacture = NumeroDeFacture;
		this.Entreprise = Entreprise;
	}

	public Date getDate() {
		return this.Date;
	}

	public String getEntreprise() {
		return this.Entreprise;
	}

	public Float getMontant() {
		return this.Montant;
	}

	public Float getMontantNonDeductible() {
		return this.MontantNonDeductible;
	}

	public String getNature() {
		return this.Nature;
	}

	public String getNumeroDevis() {
		return this.NumeroDevis;
	}

	public String getNumeroFacture() {
		return this.NumeroFacture;
	}

	public Float getReduction() {
		return this.Reduction;
	}

	public void setDate(Date date) {
		this.Date = date;
	}

	public void setEntreprise(String entreprise) {
		this.Entreprise = entreprise;
	}

	public void setMontant(Float montant) {
		this.Montant = montant;
	}

	public void setMontantNonDeductible(Float montantNonDeductible) {
		this.MontantNonDeductible = montantNonDeductible;
	}

	public void setNature(String nature) {
		this.Nature = nature;
	}

	public void setNumeroDevis(String numeroDevis) {
		this.NumeroDevis = numeroDevis;
	}

	public void setNumeroFacture(String numeroFacture) {
		this.NumeroFacture = numeroFacture;
	}

	public void setReduction(Float reduction) {
		this.Reduction = reduction;
	}
	public Float getMontantADeclarer() {
		this.MontantADeclarer=(this.Montant-this.MontantNonDeductible)*(1-this.Reduction);
		return this.MontantADeclarer;
	}
	private boolean recuperableImpots;
	private boolean recuperableLocataire;

	public void setRecuperabilite(boolean impots, boolean locataire) throws IllegalArgumentException {
		if (impots && locataire) {
			throw new IllegalArgumentException("Une facture ne peut pas être récupérable aux impôts et au locataire simultanément.");
		}
		this.recuperableImpots = impots;
		this.recuperableLocataire = locataire;
	}

	public void associerDevis(String entreprise, Float montant) throws IllegalArgumentException {
		if (this.entrepriseDevis != null && !this.entrepriseDevis.equals(entreprise)) {
			throw new IllegalArgumentException("L'entreprise ne peut pas être modifiée.");
		}
		this.entrepriseDevis = entreprise;
		this.montantDevis = montant;
	}

}
