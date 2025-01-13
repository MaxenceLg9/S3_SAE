package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.*;
import net.mpvm.saeimmobilier.util.Unfinished;

import java.sql.SQLException;
import java.util.*;
import java.sql.Date;

public class Bail extends Queryable {

	private int idBail;
	private float provisionSurCharge;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	private float depotGarantie;
	private boolean renouvelable;
	private int idBien;

	private Date dateSignature;

	public static final String DELETE_QUERY = "DELETE FROM Bail WHERE IdBail = ?";


	private Bail(int idBail, Date dateDebut, float loyer, boolean renouvelable, float totalCharge, float depotGarantie, Date dateSignature, Date dateFin, int idBien){
		this.idBail = idBail;
		this.dateDebut = dateDebut;
		this.loyer = loyer;
		this.totalCharge = totalCharge;
		this.dateFin = dateFin;
		this.dateSignature = dateSignature;
		this.depotGarantie = depotGarantie;
		this.renouvelable = renouvelable;
		this.idBien = idBien;
	}
	// Constructeur
	public Bail(Date dateDebut, float loyer, boolean renouvelable, float totalCharge, float depotGarantie, Date dateSignature, Date dateFin, int idBien){
		this(-1, dateDebut, loyer, renouvelable, totalCharge, depotGarantie, dateSignature, dateFin, idBien);
	}

	private Bail(Map<String, Object> row) {
		this((int) row.get("IdBail"),
				(Date) row.get("DateDebut"),
				(int) (float) row.get("MontantLoyer"),
				(boolean) row.get("Renouvelable"),
				(float) row.get("DepotGarantie"),
				(float) row.get("TotalCharges"),
				(Date) row.get("DateSignature"),
				(Date) row.get("DateFin"),
				(int) row.get("IdBien"));
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
			Bail bail = new Bail(row);
			baux.add(bail);
		}
	}

	public static List<Bail> getBauxFromLocataire(Locataire locataire) throws BailException {
		ArrayList<Bail> bauxList = new ArrayList<>();
		final String SELECT_QUERY = """
        SELECT B.*
        FROM Bail B
        JOIN AssocieBailLocataire ABL ON B.IdBail = ABL.IdBail
        WHERE ABL.IdLocataire = ?
    """;
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
			selectQueryElement.setArgs(Map.of(-1, locataire.getIdLocataire())); // Assuming getId() retrieves the current Locataire's ID.
			selectQueryElement.execute();
			List<Map<String, Object>> result = selectQueryElement.getResult();
			for (Map<String, Object> row : result) {
				bauxList.add(new Bail(row)); // Assuming Bail has a constructor that accepts a map of database row values.
			}
		} catch (QueryElement.QEltException qEltException) {
			qEltException.getSqlException().printStackTrace();
			throw new BailException("Erreur lors de la récupération des baux du locataire", qEltException.getSqlException());
		}
		return bauxList;

	}


	// Méthode pour savoir si le bail est en colocation
	public boolean estEnColocation() throws BailException {
		return this.getLocataires().size() > 1;
	}

	// Méthode pour diviser le loyer entre colocataires
	public Map<Locataire, Float> diviserLoyer() throws BailException {
		if (this.getLocataires().isEmpty()) {
			throw new IllegalStateException("Aucun locataire n'est associé au bail.");
		}

		Map<Locataire, Float> partsLoyer = new HashMap<>();
		if (estEnColocation()) {
			float totalPourcentage = 0;
			boolean utilisationRepartition = false;

			// Vérifier si des répartitions sont définies
			for (Locataire locataire : getLocataires()) {
				if (getRepartitionElectricite().containsKey(locataire) ||
						getRepartitionEntretien().containsKey(locataire) ||
						getRepartitionOrduresMenageres().containsKey(locataire)) {
					utilisationRepartition = true;
					totalPourcentage += getRepartitionElectricite().getOrDefault(locataire, 0f);
				}
			}

			if (utilisationRepartition && totalPourcentage > 0) {
				// Répartition en fonction des pourcentages définis
				for (Locataire locataire : getLocataires()) {
					float pourcentage = getRepartitionElectricite().getOrDefault(locataire, 0f);
					partsLoyer.put(locataire, this.loyer * pourcentage);
				}
			} else {
				// Répartition équitable
				float partEquitable = this.loyer / this.getLocataires().size();
				for (Locataire locataire : getLocataires()) {
					partsLoyer.put(locataire, partEquitable);
				}
			}
		} else {
			// Bail sans colocation : un seul locataire paie l'intégralité
			partsLoyer.put(getLocataires().getFirst(), this.loyer);
		}
		return partsLoyer;
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


	public List<Locataire> getLocataires() throws BailException {
		try {
			return Locataire.getLocatairesFromBail(this.getIdBail());
		}
		catch (Locataire.LocataireException e) {
			throw new BailException("Erreur lors de la récupération des locataires du bail", e.getSqlException());
		}
	}


	@Unfinished
	public ArrayList<Charges> getCharges() {
		return null;
		//TODO : query pour get dans la bd
	}

	@Unfinished
	public Map<Locataire, Float> getRepartitionElectricite() {
		return null;
		//TODO : query pour get dans la bd
	}

	@Unfinished
	public Map<Locataire, Float> getRepartitionOrduresMenageres() {
		return null;
		//TODO : query
	}

	@Unfinished
	public Map<Locataire, Float> getRepartitionEntretien() {
		//TODO : query
		return null;
	}

	@Unfinished
	public ArrayList<Paiement> getPaiements() {
		return null;
	}


	@Unfinished
	public void setRepartitionOrduresMenageres(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		//TODO : query to put
	}

	@Unfinished
	public void setRepartitionElectricite(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		//TODO : query pour mettre dans la bd
	}

	@Unfinished
	public void setRepartitionEntretien(Locataire locataire, float pourcentage) {
		if (pourcentage < 0 || pourcentage > 1) {
			throw new IllegalArgumentException("Pourcentage pas compris entre 0 et 1");
		}
		//TODO : query to put
	}

	@Unfinished
	public void setPaiements(ArrayList<Paiement> paiements) {
		//TODO : query
	}


	@Unfinished
	public void setLocataires(List<Locataire> locataires, Map<Locataire, Float> repartitionElectricite, Map<Locataire, Float> repartitionEntretien, Map<Locataire, Float> repartitionOrduresMenageres) throws BailException {
		if (locataires == null || locataires.isEmpty()) {
			throw new IllegalArgumentException("Locataires list cannot be null or empty.");
		}

		try (UpdateQueryElement query = new UpdateQueryElement(
				"INSERT INTO AssocieBailLocataire (IdLocataire, IdBail, RepartitionElectricite, RepartitionEntretien, RepartitionOrdures_Menageres) " +
						"VALUES (?, ?, ?, ?, ?) " +
						"ON DUPLICATE KEY UPDATE " +
						"RepartitionElectricite = VALUES(RepartitionElectricite), " +
						"RepartitionEntretien = VALUES(RepartitionEntretien), " +
						"RepartitionOrdures_Menageres = VALUES(RepartitionOrdures_Menageres)",true)) {

			for (Locataire locataire : locataires) {
				if (!repartitionElectricite.containsKey(locataire) ||
						!repartitionEntretien.containsKey(locataire) ||
						!repartitionOrduresMenageres.containsKey(locataire)) {
					throw new IllegalArgumentException("Missing repartition data for locataire: " + locataire.getNom());
				}

				query.setArgs(Map.of(
						1, locataire.getIdLocataire(),
						2, this.getIdBail(), // Assuming Bail class has a getId() method for IdBail
						3, repartitionElectricite.get(locataire),
						4, repartitionEntretien.get(locataire),
						5, repartitionOrduresMenageres.get(locataire)
				));
			}

			query.execute();
		} catch (QueryElement.QEltException e) {
			e.printStackTrace();
			throw new BailException("Failed to set locataires for bail.", e.getSqlException());
		}
	}

	@Unfinished
	public void setCharges(List<Charges> charges) {
		//TODO : ???
	}

	@Override
	public void save() throws BailException {
		if(this.getIdBail() != -1)
			throw new BailException("Le bail existe déjà dans la table", null);
		try (UpdateQueryElement query = new UpdateQueryElement("INSERT INTO Bail (DateDebut, MontantLoyer, Renouvelable, TotalCharges, DepotGarantie, DateSignature, DateFin) VALUES (?, ?, ?, ?, ?, ?, ?)", true)){
			query.setArgs(Map.of(1, this.getDateDebut(), 2, this.getLoyer(), 3, false, 4, this.getTotalCharge(), 5, this.getProvisionSurCharge(), 6, this.getDateSignature(), 7, this.getDateFin())).execute();

		}catch (QueryElement.QEltException e) {
			e.getSqlException().printStackTrace();
			throw new BailException("Erreur lors de l'insertion du bail", e.getSqlException());
		}
	}

	@Override
	public void modify() throws QbleException {

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


	public static class BailException extends QbleException {
		public BailException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}
