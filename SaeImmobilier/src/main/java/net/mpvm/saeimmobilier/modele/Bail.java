package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.util.Unfinished;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Bail extends Queryable {


	private int idBail;
	private float factureEau;
	private float totalCharge;
	private float loyer;
	private float regularisationCharge;
	private Date dateDebut;
	private Date dateFin;
	private float depotGarantie;
	private boolean renouvelable;
	private final BienLouable bienLouable;
	private String cheminFichier;

	private Date dateSignature;

	private static final String SELECT_BAUX_FROM_LOCATAIRE = "SELECT B.* FROM Bail B JOIN AssocieBailLocataire ABL ON B.IdBail = ABL.IdBail WHERE ABL.IdLocataire = ?";
	private static final String SELECT_TOTAL_LOYER = "SELECT SUM(Bail.MontantLoyer) AS TotalLoyers FROM Bail JOIN Bien B ON Bail.IdBien = B.IdBien AND Bail.Archive IS NULL";
	public static final String DELETE_QUERY = "DELETE FROM Bail WHERE IdBail = ?";
	public static final String DELETE_QUERY_BIEN = "DELETE FROM Bail WHERE IdBien = ?";


	private Bail(int idBail, Date dateDebut, float loyer, boolean renouvelable, float totalCharge, float depotGarantie, Date dateSignature, Date dateFin, BienLouable bienLouable, String cheminFichier){
		this.idBail = idBail;
		this.dateDebut = dateDebut;
		this.loyer = loyer;
		this.totalCharge = totalCharge;
		this.dateFin = dateFin;
		this.dateSignature = dateSignature;
		this.depotGarantie = depotGarantie;
		this.renouvelable = renouvelable;
		this.bienLouable = bienLouable;
		setCheminFichier(cheminFichier);
	}

	// Constructeur
	public Bail(Date dateDebut, float loyer, boolean renouvelable, float totalCharge, float depotGarantie, Date dateSignature, Date dateFin, BienLouable bienLouable, String cheminFichier){
		this(-1, dateDebut, loyer, renouvelable, totalCharge, depotGarantie, dateSignature, dateFin, bienLouable, cheminFichier);
	}

	private Bail(Map<String, Object> row) throws Bien.BienException {
		this(row,BienLouable.BLBuilder.getBienLouable((int) row.get("IdBien")));
	}

	private Bail(Map<String, Object> row, BienLouable bienLouable) {
		this((int) row.get("IdBail"),
				(Date) row.get("DateDebut"),
				JfxUtil.doubleToFloat(row.get("MontantLoyer")),
				(boolean) row.get("Renouvelable"),
				JfxUtil.doubleToFloat(row.get("DepotGarantie")),
				JfxUtil.doubleToFloat(row.get("TotalCharges")),
				(Date) row.get("DateSignature"),
				(Date) row.get("DateFin"),
				bienLouable,
				(String) row.get("CheminDocument"));
	}


	public static Bail getBailFromCharges(Charges charges) {
		try(SelectQueryElement selectQueryElement = new SelectQueryElement("SELECT * FROM Bail B JOIN Charges C ON C.IdBail = B.Bail WHERE C.IdCharges = ?")){
			selectQueryElement.setArgs(Map.of(1, charges.getIdCharges()));
			Result result = selectQueryElement.execute();
			Map<String, Object> row = result.getFirst();
			return new Bail(row);
		}
		catch (QueryElement.QEltException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Bail> getBauxFromBien(BienLouable bienLouable) throws BailException {
		try(SelectQueryElement selectQueryElement = new SelectQueryElement("SELECT * FROM Bail WHERE IdBien = ?")){
			selectQueryElement.setArgs(Map.of(1,bienLouable.getIdBien())).execute();
			return selectQueryElement.getResult().stream().map(x -> new Bail(x,bienLouable)).toList();
		}catch (QueryElement.QEltException e){
			throw new Bail.BailException("Impossible de récupérer les baux de ce bien", null);
		}
	}

	private void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public String getCheminFichier() {
		return this.cheminFichier;
	}

	public File getDocument(){
		return new File("./baux/" + this.cheminFichier);
	}

	public void openDocument() throws IOException {
		Desktop.getDesktop().open(getDocument());
	}



	public BienLouable getBienLouable(){
		return this.bienLouable;
	}

	public static List<Bail> findByBien(int idBien) throws BailException {
		List<Bail> baux = new ArrayList<>();
		String query = "SELECT * FROM Bail WHERE IdBien = ?";

		try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {
			selectQueryElement.setArgs(Map.of(1, idBien));
			Result result = selectQueryElement.execute();
			for (Map<String, Object> row : result) {
				Bail bail = new Bail(row);
				baux.add(bail);
			}
		} catch (QueryElement.QEltException e) {
			throw new BailException("Erreur lors de la récupération des baux pour le bien ID " + idBien, e.getSqlException());
		}

		return baux;
	}


	public void revaloriserLoyer(int icc){
		setLoyer(this.loyer * icc/100);
		try(UpdateQueryElement updateQueryElement = new UpdateQueryElement("UPDATE Bail SET MontantLoyer = ? WHERE IdBail = ?", true)){
			updateQueryElement.setArgs(Map.of(1, this.loyer, 2, this.idBail)).execute();
		}
		catch (QueryElement.QEltException e) {
			e.printStackTrace();
		}
	}

	public static List<Bail> getBauxFromLocataire(Locataire locataire) throws BailException {
		ArrayList<Bail> bauxList = new ArrayList<>();
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_BAUX_FROM_LOCATAIRE)) {
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
	public static double calculerLoyersProprietaire() throws Exception {
		double totalLoyers = 0.0;

		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_TOTAL_LOYER)) {
			selectQueryElement.execute();
			List<Map<String, Object>> result = selectQueryElement.getResult();

			if (!result.isEmpty() && result.getFirst().get("TotalLoyers") != null) {
				totalLoyers = (double) result.getFirst().get("TotalLoyers");
			}
		} catch (QueryElement.QEltException qEltException) {
			qEltException.getSqlException().printStackTrace();
			throw new Exception("Erreur lors du calcul des loyers pour le propriétaire.", qEltException.getSqlException());
		}

		return totalLoyers;
	}
	public static List<Bail> findAllCalculLoyers() throws Bail.BailException {
		List<Bail> bailslist = new ArrayList<>();
		String SELECT_QUERY = """
        SELECT *
        FROM Bail B
        WHERE B.ARCHIVE is null
    	""";

		try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
			Result rs = query.execute();
			for (Map<String, Object> row : rs) {
				Bail bail = new Bail(row);
				bailslist.add(bail);
			}
		} catch (QueryElement.QEltException qEltException) {
			throw new Bail.BailException("Erreur lors de la récupération des bails", qEltException.getSqlException());
		}

		return bailslist;
	}

	// Méthode pour savoir si le bail est en colocation
	public boolean isColocation() throws BailException {
		return this.getLocataires().size() > 1;
	}

	// Méthode pour diviser le loyer entre colocataires
	public Map<Locataire, Float> diviserLoyer() throws BailException {
		if (this.getLocataires().isEmpty()) {
			throw new IllegalStateException("Aucun locataire n'est associé au bail.");
		}

		Map<Locataire, Float> partsLoyer = new HashMap<>();

		Map<Locataire,AssociationBailLocataires> locatairesAssociation = Locataire.getLocatairesAssociation(this);
		if(locatairesAssociation.keySet().size() > 1){
			partsLoyer = locatairesAssociation.values().stream().collect(Collectors.toMap(AssociationBailLocataires::getLocataire, AssociationBailLocataires::getPartLoyer));
		}

		return partsLoyer;
	}

	public int getIdBail() {
		return idBail;
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
			return Locataire.getLocatairesFromBail(this);
		}
		catch (Locataire.LocataireException e) {
			throw new BailException("Erreur lors de la récupération des locataires du bail", e.getSqlException());
		}
	}


	@Unfinished
	public List<Charges> getCharges() throws Charges.ChargesException {
		return Charges.getChargesFromBail(this);
	}

	@Unfinished
	public List<Paiement> getPaiements() throws Paiement.PaiementException {
		return Paiement.getPaiements(this);
	}


	public void setLocatairesAssociation(Map<Locataire,AssociationBailLocataires> locatairesAssociation) throws BailException {
		Locataire.setLocatairesAssociation(locatairesAssociation);
	}

	public Map<Locataire, AssociationBailLocataires> getLocatairesAssociation() throws BailException {
		return Locataire.getLocatairesAssociation(this);
	}

	public float getDepotGarantie() {
		return depotGarantie;
	}

	public void setDepotGarantie(float depotGarantie) {
		this.depotGarantie = depotGarantie;
	}

	@Override
	public void save() throws BailException {
		if(this.getIdBail() != -1)
			throw new BailException("Le bail existe déjà dans la table", null);
		try (UpdateQueryElement query = new UpdateQueryElement("INSERT INTO Bail (DateDebut, MontantLoyer, Renouvelable, TotalCharges, DepotGarantie, DateSignature, DateFin, IdBien, CheminDocument) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", true)){
			query.setArgs(Map.of(1, this.getDateDebut(), 2, this.getLoyer(), 3, 1, 4, this.getTotalCharge(), 5, this.getDepotGarantie(), 6, this.getDateSignature(), 7, this.getDateFin(), 8, this.getBienLouable().getIdBien(), 9, this.getCheminFichier())).execute();
			Result rs = query.getGeneratedKeys();
			this.idBail = ((BigInteger) rs.getFirst().get("GENERATED_KEY")).intValue();
		} catch (QueryElement.QEltException e) {
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
			AssociationBailLocataires.delete(this);
			query.setArgs(Map.of(1,this.getIdBail())).execute();
			this.getDocument().delete();
			this.idBail = -1;
		}
		catch (QueryElement.QEltException e) {
			throw new Bail.BailException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}


	public static void delete(BienLouable bienLouable) throws BailException {
		try(UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY_BIEN, true)){
			query.setArgs(Map.of(1,bienLouable.getIdBien())).execute();
		}
		catch (QueryElement.QEltException e) {
			throw new Bail.BailException("Erreur lors de la suppression du bien", e.getSqlException());
		}
	}


	@Override
	public void archiver() throws QbleException {

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
