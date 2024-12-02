package net.mpvm.saeimmobilier.modele;
import net.mpvm.saeimmobilier.sql.Connection.BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Immeuble extends Bien{
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
		this.idImmeuble = idImmeuble;
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
		String query = "SELECT * FROM BienLouable WHERE IdImmeuble = ?";
		try (Connection connection = BD.getConnection();
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
}
