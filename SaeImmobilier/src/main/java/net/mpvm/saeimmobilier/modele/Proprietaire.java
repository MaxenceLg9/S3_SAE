package net.mpvm.saeimmobilier.modele;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

public class Proprietaire {
	public static final String INSERT_QUERY = "INSERT INTO proprietaire (Nom,Prenom,Telephone,Email,MotDePasse) VALUES (?,?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM proprietaire";
	public static final String DELETE_QUERY = "DELETE FROM proprietaire WHERE IdProprietaire = ?";
	public static final String SELECT_COUNT_QUERY = "SELECT COUNT(*) AS count FROM proprietaire WHERE email = ?";

	private String Nom;
	private String Prenom;
	private String Telephone;
	private String Email;
	private String MotDePasse;
	private ArrayList<Bien> biensPossedes;
	private int IdProprietaire;

	Proprietaire(int IdProprietaire,String nom,String prenom, String Telephone, String Email, String MotDePasse) throws IllegalArgumentException {
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
		this.Nom = nom;
		this.Prenom = prenom;
		this.Telephone = Telephone;
		this.MotDePasse = MotDePasse;
		this.biensPossedes = new ArrayList<>();
	}

	public Proprietaire(String nom,String prenom,String Telephone, String Email, String MotDePasse) throws IllegalArgumentException {
		this(-1,nom,prenom, Telephone, Email, MotDePasse);
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



	// Getters et setters pour les propriétés

	public String getEmail() {
		return this.Email;
	}

	public String getMotDePasse() {
		return this.MotDePasse;
	}

	public String getNom() {
		return this.Nom;
	}

	public String getPrenom() {
		return this.Prenom;
	}

	public String getTelephone() {
		return this.Telephone;
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

	public void setNom(String nom) {
		this.Nom = nom;
	}

	public void setPrenom(String prenom) {
		this.Prenom = prenom;
	}

	public void setTelephone(String telephone) {
		this.Telephone = telephone;
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
									1, this.getNom(),
									2, this.getPrenom(),
									3, this.getTelephone(),
									4, this.getEmail(),
									5, this.getMotDePasse()
							))
					.execute();
		} catch (QueryElement.QueryException queryException) {
			throw new ProprietaireException("Erreur lors de l'ajout du propriétaire : ", queryException.getSqlException());
		}
	}

	private boolean emailAlreadyExists(String email) throws ProprietaireException {
		try(SelectQueryElement queryElement = new SelectQueryElement(SELECT_COUNT_QUERY)) {
			// Remplacez SELECT_COUNT_QUERY par la requête SQL réelle pour vérifier l'existence de l'e-mail
			queryElement.setArgs(Map.of(1, email));
			// Exécution de la requête et récupération des résultats
			ResultSet results = queryElement.execute();
			results.next();
			// Récupération du champ "count" dans le premier résultat
			int count = results.getInt("count");
			return count > 0;

		} catch (QueryElement.QueryException | SQLException e) {
			throw new ProprietaireException("Erreur lors de la vérification de l'adresse e-mail : ", e instanceof SQLException ? e : ((QueryElement.QueryException) e).getSqlException());
		}
	}
	public static List<Proprietaire> findAll() throws Proprietaire.ProprietaireException {
		List<Proprietaire> p = new ArrayList<>();

		try(SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)){
			ResultSet rs = query.execute();
			while (rs.next()) {
				p.add(
						new Proprietaire(
								rs.getString("Nom"),
								rs.getString("Prenom"),
								rs.getString("Telephone"),
								rs.getString("Email"),
								rs.getString("MotDePasse")
						));
			}
		}
		catch (QueryElement.QueryException | SQLException queryException){
			throw new Proprietaire.ProprietaireException("Erreur lors de la récupération des propriétaires");
		}
		return p;
	}
	public static class ProprietaireException extends Queryable.QueryableException {
		public ProprietaireException(String message){
			super(message);
		}
		public ProprietaireException(String message, Throwable cause){
			super(message, (SQLException) cause);
		}
	}
}