package net.mpvm.saeimmobilier.modele;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import net.mpvm.saeimmobilier.util.JfxUtil;

public final class Locataire extends Queryable {

	public static final String INSERT_QUERY = "INSERT INTO Locataire (nom, prenom, email, sexe, telephone) VALUES (?, ?, ?, ?, ?)";

	public static final String SELECT_QUERY = "SELECT * FROM Locataire";
	public static final String DELETE_QUERY = "DELETE FROM Locataire WHERE IdLocataire = ?";
	public static final String UPDATE_QUERY = "UPDATE Locataire SET nom = ?, prenom = ?, email = ?, sexe = ?, telephone = ? WHERE IdLocataire = ?";
	private static final String SELECT_LOCATAIRES_REPARTITIONS_BAIL = "SELECT L.*, ABL.RepartitionElectricite, ABL.RepartitionEntretien, ABL.RepartitionOrdures_Menageres FROM Locataire L JOIN AssocieBailLocataire ABL ON L.IdLocataire = ABL.IdLocataire WHERE ABL.IdBail = ?";

	private int idLocataire;
	private char sexe;
	private String telephone;
	private String email;
	private String nom;
	private String prenom;


	private Locataire(String nom, String prenom, String email, char sexe, String telephone, int idLocataire) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.sexe = sexe;
		this.telephone = telephone;
		this.idLocataire = idLocataire;
	}

	public Locataire(String nom, String prenom, String email, char sexe, String telephone) {
		this(nom, prenom, email, sexe, telephone, -1);
	}

	private Locataire(Map<String, Object> row){
		this(row.get("Nom").toString(),
				row.get("Prenom").toString(),
				row.get("Email").toString(),
				row.get("Sexe").toString().charAt(0),
				row.get("Telephone").toString(),
				(int) row.get("IdLocataire"));
	}

//	public static void setLocatairesAssociation(Map<Locataire,AssociationBailLocataires> locatairesAssociation) throws Bail.BailException {
//		if (locatairesAssociation == null || locatairesAssociation.isEmpty()) {
//			throw new Bail.BailException("Locataires list cannot be null or empty.",null);
//		}
//		if(locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartElectricite).sum() != 100 ||
//				locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartEntretien).sum() != 100 ||
//				locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartOrduresMenageres).sum() != 100 ||
//				locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartEau).sum() != 100 ||
//				locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartLoyer).sum()*1000 != 100)
//			throw new Bail.BailException("La somme des répartitions doit être égale à 100",null);
//		if(locatairesAssociation.keySet().stream().anyMatch(x -> x.getIdLocataire() == -1))
//			throw new Bail.BailException("Un locataire n'existe pas dans la base de données",null);
public static void setLocatairesAssociation(Map<Locataire, AssociationBailLocataires> locatairesAssociation) throws Bail.BailException {
	if (locatairesAssociation == null || locatairesAssociation.isEmpty()) {
		throw new Bail.BailException("Locataires list cannot be null or empty.", null);
	}

	double sumElectricite = locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartElectricite).sum();
	double sumEntretien = locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartEntretien).sum();
	double sumOrduresMenageres = locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartOrduresMenageres).sum();
	double sumEau = locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartEau).sum();
	double sumLoyer = locatairesAssociation.values().stream().mapToDouble(AssociationBailLocataires::getPartLoyer).sum();

	if (sumElectricite != 100) {
		throw new Bail.BailException("La somme des répartitions d'électricité doit être égale à 100. Actuelle: " + sumElectricite, null);
	}
	if (sumEntretien != 100) {
		throw new Bail.BailException("La somme des répartitions d'entretien doit être égale à 100. Actuelle: " + sumEntretien, null);
	}
	if (sumOrduresMenageres != 100) {
		throw new Bail.BailException("La somme des répartitions des ordures ménagères doit être égale à 100. Actuelle: " + sumOrduresMenageres, null);
	}
	if (sumEau != 100) {
		throw new Bail.BailException("La somme des répartitions d'eau doit être égale à 100. Actuelle: " + sumEau, null);
	}
	if (sumLoyer -100 >0.01) {
		throw new Bail.BailException("La somme des répartitions de loyer doit être égale à 100. Actuelle: " + sumLoyer, null);
	}

	if (locatairesAssociation.keySet().stream().anyMatch(x -> x.getIdLocataire() == -1)) {
		throw new Bail.BailException("Un locataire n'existe pas dans la base de données", null);
	}
		Collection<Locataire> locataires = locatairesAssociation.values().stream().map(AssociationBailLocataires::getLocataire).toList();
		// Use a Set to check for duplicates
		Set<Locataire> uniqueLocataires = new HashSet<>(locataires);
		if(uniqueLocataires.size() < locataires.size()){
			throw new Bail.BailException("Duplicate locataires in list",null);
		}
		try (UpdateQueryElement query = new UpdateQueryElement(
				"INSERT INTO AssocieBailLocataire " +
						"(IdLocataire, IdBail, RepartitionElectricite, RepartitionEntretien, RepartitionOrdures_Menageres, RepartitionEau, RepartitionLoyer, DateDebut, DateFin) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ",
				true)) {
			AssociationBailLocataires.delete(locatairesAssociation.values().iterator().next().getBail());
			for(AssociationBailLocataires association : locatairesAssociation.values()){
				query.setArgs(Map.of(
						1, association.getLocataire().getIdLocataire(),
						2, association.getBail().getIdBail(),
						3, association.getPartElectricite(),
						4, association.getPartEntretien(),
						5, association.getPartOrduresMenageres(),
						6, association.getPartEau(),
						7, association.getPartLoyer(),
						8, association.getDateEntree(),
						9, association.getDateSortie()
				));


				query.execute();
			}
		} catch (QueryElement.QEltException e) {
			e.getSqlException().printStackTrace();
			throw new Bail.BailException("Failed to set locataires for bail.", e.getSqlException());
		}
	}

	public static Map<Locataire,AssociationBailLocataires> getLocatairesAssociation(Bail bail) throws Bail.BailException {
		Map<Locataire,AssociationBailLocataires> locataires = new HashMap<>();
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_LOCATAIRES_REPARTITIONS_BAIL)) {
			selectQueryElement.setArgs(Map.of(1, bail.getIdBail()));
			selectQueryElement.execute();
			List<Map<String, Object>> result = selectQueryElement.getResult();
			for (Map<String, Object> row : result) {
				Locataire locataire = new Locataire(row);
				locataires.put(locataire,new AssociationBailLocataires(locataire,bail,
						JfxUtil.doubleToFloat(row.get("RepartitionElectricite")),
						JfxUtil.doubleToFloat(row.get("RepartitionEntretien")),
						JfxUtil.doubleToFloat(row.get("RepartitionOrdures_Menageres")),
						JfxUtil.doubleToFloat(row.get("RepartitionEau")),
						JfxUtil.doubleToFloat(row.get("RepartitionLoyer"))
				));
			}
		} catch (QueryElement.QEltException qEltException) {
			qEltException.getSqlException().printStackTrace();
			throw new Bail.BailException("Erreur lors de la récupération des locataires associés au bail", qEltException.getSqlException());
		}
		return locataires;
	}

	public int getIdLocataire() {
		return this.idLocataire;
	}

	public char getSexe() {
		return this.sexe;
	}

	public void setSexe(char sexe) {
		this.sexe = sexe;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public List<Charges> getCharges() throws LocataireException {
		try {
			return Charges.getChargesFromLocataire(this);
		} catch (Charges.ChargesException e) {
			throw new LocataireException("Erreur lors de la récupération des charges",e.getSqlException());
		}
	}


//	public List<Bail> getBaux() throws Bail.BailException {
//		return Bail.getBauxFromLocataire(this);
//	}

	public static List<Locataire> getLocatairesFromBail(Bail bail) throws LocataireException {
		try(SelectQueryElement query = new SelectQueryElement("""
				SELECT L.* FROM Locataire L
				JOIN AssocieBailLocataire ABL ON L.IdLocataire = ABL.IdLocataire
				WHERE ABL.IdBail = ?
				"""))
		{
			query.setArgs(Map.of(1,bail.getIdBail()));
			query.execute();
			List<Map<String,Object>> result = query.getResult();
			List<Locataire> locataires = new ArrayList<>();
			for(Map<String,Object> row : result){
				locataires.add(new Locataire(row));
			}
			return locataires;
		}catch (QueryElement.QEltException e){
			throw new LocataireException("Erreur lors de la récupération des locataires",e.getSqlException());
		}
	}


	public void save() throws LocataireException{
		if(this.getIdLocataire() != -1)
			throw new LocataireException("Le locataire existe déjà dans la table");
		try(UpdateQueryElement query =new UpdateQueryElement(INSERT_QUERY, true)){
			query.setArgs(
							Map.of(1, this.getNom(),
									2, this.getPrenom(),
									3, this.getEmail(),
									4, Character.toString(this.getSexe()),
									5, this.getTelephone()))
					.execute();
			this.idLocataire = ((BigInteger) query.getGeneratedKeys().getFirst().get("GENERATED_KEY")).intValue();
		}
		catch (QueryElement.QEltException QEltException) {
			throw new LocataireException("Erreur lors de l'ajout du locataire", QEltException.getSqlException());
		}

	}

	public void delete() throws LocataireException {
		try{
			UpdateQueryElement query = new UpdateQueryElement("DELETE FROM AssocieBailLocataire WHERE IdLocataire=?", true);
			query.setArgs(Map.of(1,this.getIdLocataire())).execute();
			UpdateQueryElement query2 = new UpdateQueryElement(DELETE_QUERY, true);
			query2.setArgs(Map.of(1,this.getIdLocataire())).execute();
		}
		catch (QueryElement.QEltException e) {
			throw new LocataireException("Erreur lors de la suppression du locataire");
		}
	}

	@Override
	public void archiver() throws QbleException {
		//TODO : est-ce utile?
	}

	public void modify() throws LocataireException{
		if(this.getIdLocataire() == -1)
			throw new LocataireException("Vous ne pouvez pas modifier un locataire qui n'existe pas");
		try(UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)){
			query.setArgs(
					Map.of(1, this.getNom(),
							2, this.getPrenom(),
							3, this.getEmail(),
							4, Character.toString(this.getSexe()),
							5, this.getTelephone(),
							6, this.getIdLocataire())).execute();
		}catch(QueryElement.QEltException QEltException){
			throw new LocataireException("Erreur lors de la modification du locataire", QEltException.getSqlException());
		}
	}

	public String toString(){
		return "Nom : " + this.getNom() + ", Prenom : " + this.getPrenom() + ", Email : " + this.getEmail();
	}

	public static List<Locataire> findAll() throws LocataireException {
		List<Locataire> l = new ArrayList<>();

		try(SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)){
			query.execute();
			List<Map<String,Object>> result = query.getResult();
			for(Map<String, Object> row : result){
				l.add(
						new Locataire(row.get("Nom").toString(),
								row.get("Prenom").toString(),
								row.get("Email").toString(),
								row.get("Sexe").toString().charAt(0),
								row.get("Telephone").toString(),
								(int) row.get("IdLocataire")));
			}
		}
		catch (QueryElement.QEltException qEltException){
			throw new LocataireException("Erreur lors de la récupération des locataires", qEltException.getSqlException());
		}
		return l;
	}




	public static class LocataireException extends QbleException {

		public LocataireException(String message){
			this(message,null);
		}

		public LocataireException(String message, SQLException sqlException){
			super(message,sqlException);
		}
	}
}