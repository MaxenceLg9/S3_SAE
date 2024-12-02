package net.mpvm.saeimmobilier.modele;
import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
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


	private int idImmeuble;
	private List<BienLouable> biensAssocies;
	private List<Travaux> travauxAssocies;

	private Immeuble( String ville, int codePostal, String adresse, int idImmeuble) {
		super( ville, codePostal, adresse); // Initialisation des attributs hérités de Bien
		this.idImmeuble = idImmeuble;
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}

	public Immeuble(String ville, int codePostal, String adresse) {
		super(ville, codePostal, adresse); // Utilisation du constructeur de Bien
		this.biensAssocies = new ArrayList<>();
		this.travauxAssocies = new ArrayList<>();
	}


	// Getters et Setters

	public int getIdImmeuble() {
		return idImmeuble;
	}

	public void setIdImmeuble(int idImmeuble) {
		try{
			this.idImmeuble = new SelectQueryElement(SELECT_WHERE_QUERY).setArgs(
					Map.of(
							1,this.getAdresse(),
							2,this.getVille(),
							3,this.getCodePostal()
			)).execute().getInt("idImmeuble");
		} catch (QueryElement.QueryException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ;
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
		String query = "SELECT * FROM BienLouable";
		try (Connection connection = BD.getConnection(true);
			 PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, this.idImmeuble);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				BienLouable bien = new BienLouable(
						rs.getString("Ville"),
						rs.getInt("CodePostal"),
						rs.getString("Adresse"),
						rs.getInt("IdBienLouable"),
						rs.getInt("NbPieces"),
						rs.getInt("NumeroFiscal"),
						null, // Vous pouvez ajouter l'immeuble actuel
						rs.getFloat("Surface")
				);
				biensAssocies.add(bien);
			}
		} catch (Exception e) {
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
		try{
			new UpdateQueryElement(INSERT_QUERY, true)
					.setArgs(
							Map.of(1, this.getAdresse(),
									2, this.getVille(),
									3, this.getCodePostal())).execute();
		}
		catch (QueryElement.QueryException sqlE){
			throw new Queryable.QueryableException("Erreur lors de l'ajout du bien");
		}
	}
}
