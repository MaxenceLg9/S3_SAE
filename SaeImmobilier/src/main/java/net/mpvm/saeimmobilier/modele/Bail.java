package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bail extends Queryable {
	private int idBail;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	private ArrayList<BienLouable> biens;
	private ArrayList<Locataire> locataires;
	private ArrayList<Charges> charges;
	private ArrayList<Paiement> paiements;
	private Map<Locataire, Float> repartitionElectricite;
	private Map<Locataire, Float> repartitionOrduresMenageres;
	private Map<Locataire, Float> repartitionEntretien;

	private boolean colocation;
	private Date dateSignature;

	public static final String DELETE_QUERY = "DELETE FROM Bail WHERE IdBail = ?";


	private Bail(int idBail, Date dateDebut){
		this.idBail = idBail;
		this.dateDebut = dateDebut;
		this.biens = new ArrayList<>();
		this.locataires = new ArrayList<>();
		this.charges = new ArrayList<>();
		this.paiements = new ArrayList<>();
		this.repartitionElectricite = new HashMap<>();
		this.repartitionEntretien = new HashMap<>();
		this.colocation = false;
	}
	// Constructeur
	public Bail(Date dateDebut) {
		this.dateDebut = dateDebut;
		this.biens = new ArrayList<>();
		this.repartitionOrduresMenageres = new HashMap<>();
		this.charges = new ArrayList<>();
		this.locataires = new ArrayList<>();
		this.paiements = new ArrayList<>();
		this.repartitionElectricite = new HashMap<>();
		this.repartitionEntretien = new HashMap<>();
		this.colocation = false;
	}

	public static List<Bail> findByBien(int idBien) throws BailException {
		List<Bail> baux = new ArrayList<>();
		String query = "SELECT * FROM Bail WHERE IdBien = ?";

		try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {
			selectQueryElement.setArgs(Map.of(1, idBien));
			Result result = selectQueryElement.execute();
			sortResult(baux, result);
		} catch (QueryElement.QEltException e) {
			throw new BailException("Erreur lors de la récupération des baux pour le bien ID " + idBien, e.getSqlException());
		}

		return baux;
	}

	private static void sortResult(List<Bail> baux, Result result) throws QueryElement.QEltException {
		for (Map<String, Object> row : result) {
			Bail bail = new Bail(
					(int) row.get("IdBail"),
					(Date) row.get("DateDebut")
			);

			bail.setDateFin((Date) row.get("DateFin"));
			bail.setLoyer((float) row.get("MontantLoyer"));
			bail.setColocation((boolean) row.get("Colocation"));
			bail.setDateSignature((Date) row.get("DateSignature"));

			baux.add(bail);
		}
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
	public void ajouterBien(BienLouable bien) {
		this.biens.add(bien);
	}

	// Méthode pour ajouter un locataire
	public void ajouterLocataire(Locataire locataire) {
		this.locataires.add(locataire);
	}


	public int getIdBail() {
		return idBail;
	}

	public void setIdBail(int idBail) {
		this.idBail = idBail;
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

	public ArrayList<BienLouable> getBiens() {
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

	public ArrayList<Paiement> getPaiements() {
		return paiements;
	}
	public void setPaiements(ArrayList<Paiement> paiements) {
		this.paiements = paiements;
	}

	public void setRepartitionElectricite(Map<Locataire, Float> repartitionElectricite) {
		this.repartitionElectricite = repartitionElectricite;
	}

	public void setBiens(ArrayList<BienLouable> biens) {
		this.biens = biens;
	}
	public void setLocataires(ArrayList<Locataire> locataires) {
		this.locataires = locataires;
	}
	public void setCharges(ArrayList<Charges> charges) {
		this.charges = charges;
	}

	public boolean isColocation() {
		return colocation;
	}
	public void setColocation(boolean colocation) {
		this.colocation = colocation;
	}

	public void setRepartitionEntretien(Map<Locataire, Float> repartitionEntretien) {
		this.repartitionEntretien = repartitionEntretien;
	}

	public void setRepartitionOrduresMenageres(Map<Locataire, Float> repartitionOrduresMenageres) {
		this.repartitionOrduresMenageres = repartitionOrduresMenageres;
	}

	@Override
	public void save() throws QbleException {

	}

	@Override
	public void modify(int idbien) throws QbleException {

	}

	@Override
	public void delete() throws Bail.BailException {
		if(this.getIdBail() == -1)
			throw new Bail.BailException("Le bail n'existe pas dans la table", null);
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)){
			query.setArgs(Map.of(1,this.getIdBail())).execute();
		}
		catch (QueryElement.QEltException e) {
			throw new Bail.BailException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}

	@Override
	public void archiver() throws QbleException {

	}

	protected void setId(int id) throws QbleException {
		this.idBail=id;
	}

	public int selectId() throws QbleException {
		return 0;
	}


	public Date getDateSignature() {
		return this.dateSignature;
	}
	public void setDateSignature(Date dateSignature) {
		this.dateSignature = dateSignature;
	}

	public Boolean getColocation() {
		return this.colocation;
	}
	public void setColocation(Boolean colocation) {
		this.colocation = colocation;
	}

	public static class BailException extends QbleException {
		public BailException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}
