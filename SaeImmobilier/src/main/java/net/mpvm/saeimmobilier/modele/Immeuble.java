package net.mpvm.saeimmobilier.modele;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	static Immeuble mapResultSetToImmeuble(ResultSet resultSet) throws SQLException, SQLException {
		int idImmeuble = resultSet.getInt("IdBien"); // Si IdBien correspond à l'identifiant unique de l'immeuble
		String ville = resultSet.getString("Ville");
		int codePostal = resultSet.getInt("CodePostal");
		String adresse = resultSet.getString("Adresse");

		return new Immeuble(ville, codePostal, adresse, idImmeuble);
	}

	// Méthode : récupérer les biens louables d’un immeuble
	@Override
	public String toString(){
		return this.getAdresse() + " " + this.getVille() + ", " + this.getCodePostal();
	}
}
