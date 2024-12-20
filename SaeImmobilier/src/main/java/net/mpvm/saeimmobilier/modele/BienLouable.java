package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public abstract class BienLouable extends Bien {

	public static final String INSERT_QUERY = "INSERT INTO bien (ComplementAdresse, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout, IdImmeuble) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM bien";
	public static final String DELETE_QUERY = "DELETE FROM bien WHERE IdBien = ?";
	public static final String UPDATE_QUERY = "UPDATE bien SET ComplementAdresse = ?, Surface = ?, NombrePieces = ? , NumeroFiscal = ? WHERE IdBien = ?";


	private String complementAdresse;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	private int ancienIndex;
	private boolean changementCompteur;
	private float surface;
	private Immeuble immeuble;
	private int nbPieces;




	public BienLouable(String complementAdresse,int nbPieces, String numeroFiscal, Immeuble immeuble, float surface, java.sql.Date dateAjout, int idBien) throws BienException {// Initialisation des attributs hérités de Bien
		super(idBien, numeroFiscal, dateAjout);
		this.complementAdresse = complementAdresse;
		if(immeuble == null)
			throw new BienLouableException("L'immeuble doit être renseigné", null);
		this.immeuble = immeuble;
		this.surface = surface;
		this.nbPieces = nbPieces;
		this.travaux = new ArrayList<>();
		this.baux = new ArrayList<>();
	}

	// Getters et Setters pour tous les champs

	public String getComplementAdresse() {
		return complementAdresse;
	}

	public void setComplementAdresse(String complementAdresse) {
		this.complementAdresse = complementAdresse;
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
	public int getCodePostal(){
		return this.immeuble.getCodePostal();
	}

	@Override
	public void setCodePostal(int codePostal) {
		this.immeuble.setCodePostal(codePostal);
	}

	@Override
	public String getAdresse() {
		return this.immeuble.getAdresse();
	}

	@Override
	public void setAdresse(String adresse){
		this.immeuble.setAdresse(adresse);
	}

	@Override
	public String getVille() {
		return this.immeuble.getVille();
	}

	@Override
	public void setVille(String ville) {
		this.immeuble.setVille(ville);
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

	public Immeuble getImmeuble() {
		return immeuble;
	}

	public void setImmeuble(Immeuble immeuble) {
		this.immeuble = immeuble;
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
	public void save() throws Bien.BienException {
		if(this.getIdBien() != -1)
			throw new BienException("Le bien existe déjà !",null);
		try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(INSERT_QUERY, true)){
			updateQueryElement.setArgs(
					Map.of(1,this.getComplementAdresse(),
							2, this.getAdresse(),
							3, this.getVille(),
							4, this.getCodePostal(),
							5, this.getTypeBienString(),
							6, this.getSurface(),
							7, this.getNbPieces(),
							8, this.getNumeroFiscal(),
							9, this.getDateAjout(),
							10, this.getImmeuble().getIdBien()
					)).execute();
			super.save();
		}
		catch (QueryElement.QEltException QEltException){
			throw new BienException("Erreur lors de l'ajout du bien : " + QEltException.getSqlException().getMessage(), QEltException.getSqlException());
		}

	}

	@Override
	public void modify() throws BienLouableException {
		if(getIdBien() == -1)
			throw new BienLouableException("Il faut sauvegarder le bien avant de vouloir le modifier",null);
		try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(UPDATE_QUERY, true)){
			updateQueryElement.setArgs(
					Map.of(1, this.getComplementAdresse(),
							2, this.getSurface(),
							3, this.getNbPieces(),
							4, this.getNumeroFiscal(),
							5, this.getIdBien()
					)).execute();
		}catch(QueryElement.QEltException QEltException){
			throw new BienLouableException("Erreur lors de la modification du bien : " + QEltException.getMessage(), QEltException.getSqlException());
		}
	}

	@Override
	public void delete() throws BienLouableException {
		try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(DELETE_QUERY, true)){
			updateQueryElement.setArgs(
							Map.of(1,this.getIdBien()))
					.execute();
		}catch(QueryElement.QEltException QEltException){
			throw new BienLouableException("Erreur lors de la suppression du bien", QEltException.getSqlException());
		}
	}
	public abstract static class BLBuilder extends BBuilder{

		private final String complementAdresse;
		private final int nbPieces;
		private final float surface;
		private final Immeuble immeuble;

		public BLBuilder(String complementAdresse,int nbPieces, String numeroFiscal, Immeuble immeuble, float surface, java.sql.Date dateAjout, int idBien) {
			super(idBien, numeroFiscal, dateAjout);
			this.complementAdresse = complementAdresse;
			this.nbPieces = nbPieces;
			this.surface = surface;
			this.immeuble = immeuble;
		}

        public float getSurface() {
            return surface;
        }

        public int getNbPieces() {
            return nbPieces;
        }

        public String getComplementAdresse() {
            return complementAdresse;
        }

		public Immeuble getImmeuble() {
			return immeuble;
		}
	}

	public static class BienLouableException extends BienException{

		public BienLouableException(String message, SQLException sqlException){
			super(message,sqlException);
		}
	}
}
