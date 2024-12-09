package net.mpvm.saeimmobilier.modele;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Immeuble extends Bien{

	public static final String INSERT_QUERY = "INSERT INTO immeuble (Adresse, Ville, CodePostal) VALUES (?, ?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM immeuble";
	public static final String SELECT_WHERE_QUERY = "SELECT * FROM immeuble WHERE Adresse = ? AND Ville = ? AND CodePostal = ?";
	public static final String DELETE_QUERY = "DELETE FROM immeuble WHERE idImmeuble = ?";
	public static final String UPDATE_QUERY = "UPDATE immeuble SET Adresse = ?, Ville = ?, CodePostal = ?";
	public static final String SELECT_FROM_BIEN = "SELECT * FROM bien";


	private int idImmeuble;
	private List<BienLouable> biensAssocies;
	private List<Travaux> travauxAssocies;

	Immeuble( String ville, int codePostal, String adresse, int idImmeuble) {
		super( ville, codePostal, adresse); // Initialisation des attributs hérités de Bien
		this.idImmeuble = idImmeuble;
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	public Immeuble(String ville, int codePostal, String adresse) {
		super(ville, codePostal, adresse,-1);
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.IMMEUBLE;
	}

	@Override
	public String getTypeBienString() {
		return "IMMEUBLE";
	}


	// Getters et Setters

	public int getIdImmeuble() {
		return idImmeuble;
	}

	public void setIdImmeuble(int idImmeuble) {
		try(SelectQueryElement query= new SelectQueryElement(SELECT_WHERE_QUERY)){
			query.setArgs(
					Map.of(
							1,this.getAdresse(),
							2,this.getVille(),
							3,this.getCodePostal()
					)).execute().getInt("idImmeuble");
		} catch (QueryElement.QueryException | SQLException e) {
			throw new RuntimeException(e);
		}
	}



	public void setBiensAssocies(List<BienLouable> biensAssocies) {
		this.biensAssocies = biensAssocies;
	}

	public List<Travaux> getTravauxAssocies() {
		return travauxAssocies;
	}

	public void setTravauxAssocies(List<Travaux> travauxAssocies) {
		this.travauxAssocies = travauxAssocies;
	}

	public List<BienLouable> getBiensAssocies() throws BienException {
		try (SelectQueryElement query = new SelectQueryElement(SELECT_FROM_BIEN)) {
			query.setArgs(Map.of(1, this.idImmeuble));
			ResultSet rs = query.execute();
			while (rs.next()) {
				BienLouable bien;
				if(rs.getString("TypeBien").equals("Habitation")) {
					bien = new Habitation(rs.getString("Lieu_Immeuble"),
							rs.getString("Ville"),
							rs.getInt("CodePostal"),
							rs.getString("Adresse"),
							rs.getInt("NbPieces"),
							rs.getString("NumeroFiscal"),
							null,
							rs.getFloat("Surface"),
							rs.getDate("DateAjout"));
					biensAssocies.add(bien);
				}else{
					bien = new Garage(rs.getString("Lieu_Immeuble"),
							rs.getString("Ville"),
							rs.getInt("CodePostal"),
							rs.getString("Adresse"),
							rs.getInt("NbPieces"),
							rs.getString("NumeroFiscal"),
							null,
							rs.getFloat("Surface"),
							rs.getDate("DateAjout"));
					biensAssocies.add(bien);
				}
			}
		} catch (QueryElement.QueryException | SQLException e) {
			throw new BienException("Erreur lors de la récupération des biens associés", e);
		}
		return biensAssocies;
	}
	@Override
	public String toString(){
		return this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}

	@Override
	public void save() throws QueryableException {
		if(this.getIdImmeuble() != -1)
			throw new Bien.QueryableException("Le bien existe déjà dans la table");
		try(UpdateQueryElement q = new UpdateQueryElement(INSERT_QUERY, true)){
			q.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal()))
					.execute();
		}
		catch (QueryElement.QueryException sqlE){
			throw new Queryable.QueryableException("Erreur lors de l'ajout du bien");
		}
	}

	@Override
	public void modify() throws QueryableException {

	}

	@Override
	public void delete() throws QueryableException {

	}
}
