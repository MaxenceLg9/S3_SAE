package net.mpvm.saeimmobilier.modele;

import java.sql.SQLException;
import java.sql.Date;
import java.util.Map;
import net.mpvm.saeimmobilier.sql.Query.*;

public class Travaux extends Queryable {
	private String numeroFacture;
	private String entreprise;
	private Float montant;
	private Float montantNonDeductible;
	private Float reduction;
	private String nature;
	private String numeroDevis;
	private Float montantADeclarer;
	private String entrepriseDevis;
	private Float montantDevis;
	private Date dateTravaux;
	private boolean recuperableImpots;
	private boolean recuperableLocataire;


	private Travaux(TBuilder builder) {
		this.numeroFacture = builder.numeroFacture;
		this.entreprise = builder.entreprise;
		this.montant = builder.montant;
		this.montantNonDeductible = builder.montantNonDeductible;
		this.reduction = builder.reduction;
		this.nature = builder.nature;
		this.numeroDevis = builder.numeroDevis;
		this.dateTravaux = builder.dateTravaux;
		this.recuperableImpots = builder.recuperableImpots;
		this.recuperableLocataire = builder.recuperableLocataire;
	}
	@Override
	public void save() throws Travaux.TravauxException{
		if (this.numeroFacture == null || this.numeroFacture.isEmpty()) {
			throw new TravauxException("Le numéro de facture est obligatoire pour sauvegarder un travail.");
		}
		if (this.entreprise == null || this.entreprise.isEmpty()) {
			throw new TravauxException("L'entreprise est obligatoire pour sauvegarder un travail.");
		}

		String INSERT_QUERY = """
        INSERT INTO Travaux (NumeroFacture, Entreprise, Montant, MontantNonDeductible, Reduction, Nature, NumeroDevis, DateTravaux)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

		try (UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)) {
			query.setArgs(Map.of(
					1, this.numeroFacture,
					2, this.entreprise,
					3, this.montant,
					4, this.montantNonDeductible,
					5, this.reduction,
					6, this.nature,
					7, this.numeroDevis,
					8, this.dateTravaux
			)).execute();

		} catch (QueryElement.QEltException qEltException) {
			throw new TravauxException(
					"Erreur lors de l'ajout des travaux : " + qEltException.getSqlException().getMessage(),
					qEltException.getSqlException()
			);
		}
	}


	@Override
	public void modify() throws QbleException {

	}

	@Override
	public void delete() throws QbleException {

	}

	@Override
	public void archiver() throws QbleException {

	}

	public static class TBuilder {
		private String numeroFacture;
		private String entreprise;
		private Float montant;
		private Float montantNonDeductible;
		private Float reduction;
		private String nature;
		private String numeroDevis;
		private Date dateTravaux;
		private boolean recuperableImpots;
		private boolean recuperableLocataire;

		public TBuilder(String numeroFacture, String entreprise, Date dateTravaux) {
			this.numeroFacture = numeroFacture;
			this.entreprise = entreprise;
			this.dateTravaux = dateTravaux;
		}

		public TBuilder setMontant(Float montant) {
			this.montant = montant;
			return this;
		}

		public TBuilder setMontantNonDeductible(Float montantNonDeductible) {
			this.montantNonDeductible = montantNonDeductible;
			return this;
		}

		public TBuilder setReduction(Float reduction) {
			this.reduction = reduction;
			return this;
		}

		public TBuilder setNature(String nature) {
			this.nature = nature;
			return this;
		}

		public TBuilder setNumeroDevis(String numeroDevis) {
			this.numeroDevis = numeroDevis;
			return this;
		}

		public TBuilder setRecuperabilite(boolean recuperableImpots, boolean recuperableLocataire) {
			if (recuperableImpots && recuperableLocataire) {
				throw new IllegalArgumentException("Une facture ne peut pas être récupérable aux impôts et au locataire simultanément.");
			}
			this.recuperableImpots = recuperableImpots;
			this.recuperableLocataire = recuperableLocataire;
			return this;
		}

		public Travaux build() {
			return new Travaux(this);
		}
	}

	public static class TravauxException extends Queryable.QbleException {
		public TravauxException(String message) {
			super(message);
		}

		public TravauxException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
	public Date getDateTravaux() {
		return dateTravaux;
	}

	public void setDateTravaux(Date dateTravaux) {
		this.dateTravaux = dateTravaux;
	}

	public String getEntreprise() {
		return this.entreprise;
	}

	public Float getMontant() {
		return this.montant;
	}

	public Float getMontantNonDeductible() {
		return this.montantNonDeductible;
	}

	public String getNature() {
		return this.nature;
	}

	public String getNumeroDevis() {
		return this.numeroDevis;
	}

	public String getNumeroFacture() {
		return this.numeroFacture;
	}

	public Float getReduction() {
		return this.reduction;
	}

	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}

	public void setMontant(Float montant) {
		this.montant = montant;
	}

	public void setMontantNonDeductible(Float montantNonDeductible) {
		this.montantNonDeductible = montantNonDeductible;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public void setNumeroDevis(String numeroDevis) {
		this.numeroDevis = numeroDevis;
	}

	public void setNumeroFacture(String numeroFacture) {
		this.numeroFacture = numeroFacture;
	}

	public void setReduction(Float reduction) {
		this.reduction = reduction;
	}
	public Float getMontantADeclarer() {
		this.montantADeclarer=(this.montant-this.montantNonDeductible)*(1-this.reduction);
		return this.montantADeclarer;
	}


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

	public boolean isRecuperableImpots() {
		return recuperableImpots;
	}

	public boolean isRecuperableLocataire() {
		return recuperableLocataire;
	}

	public Float getMontantDevis() {
		return montantDevis;
	}

	public String getEntrepriseDevis() {
		return entrepriseDevis;
	}
	public void setEntrepriseDevis(String entrepriseDevis) {
		this.entrepriseDevis = entrepriseDevis;
	}

	public void setMontantADeclarer(Float montantADeclarer) {
		this.montantADeclarer = montantADeclarer;
	}

	public void setMontantDevis(Float montantDevis) {
		this.montantDevis = montantDevis;
	}

	public void setRecuperableImpots(boolean recuperableImpots) {
		this.recuperableImpots = recuperableImpots;
	}
	public void setRecuperableLocataire(boolean recuperableLocataire) {
		this.recuperableLocataire = recuperableLocataire;
	}
}
