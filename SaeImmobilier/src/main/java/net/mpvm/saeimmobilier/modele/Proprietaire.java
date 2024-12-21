package net.mpvm.saeimmobilier.modele;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mpvm.saeimmobilier.sql.Query.*;

public class Proprietaire {
	public static final String INSERT_QUERY = "INSERT INTO proprietaire (Email,MotDePasse) VALUES (?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM proprietaire";
	public static final String DELETE_QUERY = "DELETE FROM proprietaire WHERE IdProprietaire = ?";
	public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) AS count FROM proprietaire WHERE email = ?";


	private String Email;
	private String MotDePasse;
	private ArrayList<Bien> biensPossedes;
	private int IdProprietaire;

	Proprietaire(int IdProprietaire,String Email, String MotDePasse) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.Email = Email;
		this.IdProprietaire = IdProprietaire;
		this.MotDePasse = MotDePasse;
		this.biensPossedes = new ArrayList<>();
	}

	public Proprietaire(String Email, String MotDePasse) throws IllegalArgumentException {
		this(-1, Email, MotDePasse);
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}

		this.biensPossedes = new ArrayList<>();
	}



	// Getters et setters pour les propriétaires

	public String getEmail() {
		return this.Email;
	}

	public String getMotDePasse() {
		return this.MotDePasse;
	}


	public void setEmail(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		this.Email = email;
	}

	public void setMotDePasse(String motDePasse) {
		if (motDePasse.length() < 8 || motDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.MotDePasse = motDePasse;
	}


	public boolean verifierMontantRegularisation(float sommeVersee, float sommeDue) {
		return Math.abs(sommeVersee - sommeDue) < 0.01; // Tolérance pour arrondis
	}

	public int getIdProprietaire() {
		return IdProprietaire;
	}

	public void setIdProprietaire(int idProprietaire) {
		IdProprietaire = idProprietaire;
	}

	public ArrayList<Bien> getBiensPossedes() {
		return biensPossedes;
	}

	public void setBiensPossedes(ArrayList<Bien> biensPossedes) {
		this.biensPossedes = biensPossedes;
	}


	public void save() throws ProprietaireException {
		try (UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)) {
			query.setArgs(
							Map.of(
									1, this.getEmail(),
									2, this.getMotDePasse()
							))
					.execute();
		} catch (QueryElement.QEltException QEltException) {
			throw new ProprietaireException("Erreur lors de l'ajout du propriétaire : ", QEltException.getSqlException());
		}
	}

	private boolean emailAlreadyExists(String email) throws ProprietaireException {
		try(SelectQueryElement queryElement = new SelectQueryElement(SELECT_COUNT_QUERY)) {
			// Remplacez SELECT_COUNT_QUERY par la requête SQL réelle pour vérifier l'existence de l'e-mail
			queryElement.setArgs(Map.of(1, email));
			// Exécution de la requête et récupération des résultats
			Map<String,Object> results = queryElement.execute().getFirst();
			// Récupération du champ "count" dans le premier résultat
			return (int) results.get("Count") > 0;

		} catch (QueryElement.QEltException qEltException) {
			throw new ProprietaireException("Erreur lors de la vérification de l'adresse qEltException-mail : ", qEltException.getSqlException());
		}
	}
	public static List<Proprietaire> findAll() throws Proprietaire.ProprietaireException {
		List<Proprietaire> p = new ArrayList<>();

		try(SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)){
			Result rs = query.execute();
			for(Map<String,Object> row : rs) {
				p.add(
						new Proprietaire(
								row.get("Email").toString(),
								row.get("MotDePasse").toString()
						));
			}
		}
		catch (QueryElement.QEltException qEltException){
			throw new Proprietaire.ProprietaireException("Erreur lors de la récupération des propriétaires");
		}
		return p;
	}
	public static class ProprietaireException extends Queryable.QbleException {
		public ProprietaireException(String message){
			super(message);
		}
		public ProprietaireException(String message, Throwable cause){
			super(message, (SQLException) cause);
		}
	}
}