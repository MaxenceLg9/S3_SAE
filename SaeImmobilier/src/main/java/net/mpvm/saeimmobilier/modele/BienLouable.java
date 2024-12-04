package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public abstract class BienLouable extends Bien {

	public static final String INSERT_QUERY = "INSERT INTO bien (Lieu_Immeuble, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout) VALUES (?, ?, ?, ?, ?,?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM bien";
	public static final String DELETE_QUERY = "DELETE FROM bien WHERE IdBien = ?";
	public static final String UPDATE_QUERY = "UPDATE bien SET Lieu_Immeuble = ?, Adresse = ?, Ville = ?, CodePostal = ?, TypeBien = ?, Surface = ?, NombrePieces = ? , NumeroFiscal = ? , DateAjout = ?";


	private String lieuImmeuble;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private float surface;
	private String numeroFiscal;
	private Immeuble immeuble;
	private Proprietaire proprietaire;
	private int nbPieces;
	private int codePostal;

	public Date getDateAjout() {
		return DateAjout;
	}

	public void setDateAjout(java.sql.Date dateAjout) {
		DateAjout = dateAjout;
	}

	private java.sql.Date DateAjout;


	BienLouable(String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, int idBienLouable, java.sql.Date dateAjout) {// Initialisation des attributs hérités de Bien
		super(ville, codePostal, adresse, -1);
		this.lieuImmeuble = lieuImmeuble;
		this.immeuble = immeuble;
		this.surface = surface;
		this.nbPieces = nbPieces;
		this.codePostal = codePostal;
		this.numeroFiscal = NumeroFiscal;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
		this.DateAjout = dateAjout;
	}

	public BienLouable(String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, java.sql.Date dateAjout) {
		this(ville,codePostal,adresse,nbPieces,NumeroFiscal,immeuble,surface,-1,dateAjout);
	}

	// Getters et Setters pour tous les champs

	public String getLieuImmeuble() {
		return lieuImmeuble;
	}

	public void setLieuImmeuble(String lieuImmeuble) {
		this.lieuImmeuble = lieuImmeuble;
	}

	public ArrayList<Travaux> getTravaux() {
		return travaux;
	}

	public void ajouterTravaux(Travaux travail) {
		this.travaux.add(travail);
	}

	public ArrayList<Bail> getBaux() {
		return baux;
	}

	@Override
	public int getCodePostal() {
		return codePostal;
	}

	@Override
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}

	public int getAncienIndex() {
		return ancienIndex;
	}

	public void setAncienIndex(int ancienIndex) {
		this.ancienIndex = ancienIndex;
	}

	public boolean isChangementCompteur() {
		return changementCompteur;
	}

	public void setChangementCompteur(boolean changementCompteur) {
		this.changementCompteur = changementCompteur;
	}

	public void setSurface(float surface) {
		this.surface = surface;
	}

	public float getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		if (surface <= 0) {
			throw new IllegalArgumentException("La surface doit être positive.");
		}
		this.surface = surface;
	}

	public String getNumeroFiscal() {
		return numeroFiscal;
	}

	public void setNumeroFiscal(String numeroFiscal) {
		if (numeroFiscal == null || numeroFiscal.length() != 12) {
			throw new IllegalArgumentException("Le numéro fiscal doit être de 12 caractères.");
		}
		this.numeroFiscal = numeroFiscal;
	}

	public Immeuble getImmeuble() {
		return immeuble;
	}

	public void setImmeuble(Immeuble immeuble) {
		this.immeuble = immeuble;
	}

	public Proprietaire getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Proprietaire proprietaire) {
		this.proprietaire = proprietaire;
	}

	public void setTravaux(ArrayList<Travaux> travaux) {
		this.travaux = travaux;
	}

	public void setBaux(ArrayList<Bail> baux) {
		this.baux = baux;
	}
	public int getNbPieces() {
		return nbPieces;
	}
	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}

	@Override
	public void save() throws QueryableException {
		if(this.getIdBien() != -1)
			throw new Queryable.QueryableException("Le bien existe déjà dans la table");
		try(UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)){
			query.setArgs(
					Map.of(1, "A",
							2, this.getAdresse(),
							3, this.getVille(),
							4, this.getCodePostal(),
							5, this.getTypeBienString(),
							6, this.getSurface(),
							7, this.getNbPieces(),
							8, this.getNumeroFiscal(),
							9, this.getDateAjout()
					)).execute();
		}
		catch (QueryElement.QueryException sqlE){
			sqlE.getCause().printStackTrace();
			throw new Locataire.LocataireException("Erreur lors de l'ajout du bien");
		}

	}

	@Override
	public void modify() throws QueryableException {

	}

	@Override
	public void delete() throws QueryableException {

	}
}
