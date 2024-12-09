package net.mpvm.saeimmobilier.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

public class Proprietaire {
	public static final String INSERT_QUERY = "INSERT INTO Proprietaire (Email,MotDePasse) VALUES (?, ?)";
	public static final String SELECT_QUERY = "SELECT * FROM Proprietaire";
	public static final String DELETE_QUERY = "DELETE FROM Proprietaire WHERE IdProprietaire = ?";

	private String Nom;
	private String Prenom;
	private String Telephone;
	private String Email;
	private String MotDePasse;
	private String Ville;
	private Integer CodePostal;
	private String Adresse;
	private ArrayList<Bien> biensPossedes;
	private int IdProprietaire;
	private Proprietaire(int IdProprietaire,String Email,String MotDePasse) throws IllegalArgumentException {
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
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(Email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Email non valide");
		}
		if (MotDePasse.length() < 8 || MotDePasse.length() > 24) {
			throw new IllegalArgumentException("Le mot de passe doit être compris entre 8 et 24 caractères.");
		}
		this.Email = Email;
		this.MotDePasse = MotDePasse;
		this.biensPossedes = new ArrayList<>();
	}

	// Getters et setters pour les propriétés

	public String getAdresse() {
		return this.Adresse;
	}

	public Integer getCodePostal() {
		return this.CodePostal;
	}

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

	public String getVille() {
		return this.Ville;
	}

	public void setAdresse(String adresse) {
		this.Adresse = adresse;
	}

	public void setCodePostal(Integer codePostal) {
		this.CodePostal = codePostal;
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

	public void setVille(String ville) {
		this.Ville = ville;
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
		try {
			if (this.getIdProprietaire() == -1) {
				// Vérification préalable : si l'adresse e-mail existe déjà
				if (emailAlreadyExists(this.getEmail())) {
					throw new ProprietaireException("Cette adresse e-mail est déjà utilisée.");
				}

				// Si l'adresse n'existe pas, insertion
				new UpdateQueryElement(INSERT_QUERY, true)
						.setArgs(
								Map.of(
										1, this.getEmail(),
										2, this.getMotDePasse()
								))
						.execute();
			} else {
				throw new ProprietaireException("Le propriétaire existe déjà dans la table.");
			}
		} catch (QueryElement.QueryException sqlE) {
			throw new ProprietaireException("Erreur lors de l'ajout du propriétaire : " + sqlE.getMessage());
		}
	}

	/**
	 * Vérifie si l'e-mail existe déjà dans la base de données.
	 * @param email Adresse e-mail à vérifier.
	 * @return true si l'e-mail existe déjà, sinon false.
	 * @throws ProprietaireException si une erreur SQL survient.
	 */
	private boolean emailAlreadyExists(String email) throws ProprietaireException {
		try {
			// Remplacez SELECT_COUNT_QUERY par la requête SQL réelle pour vérifier l'existence de l'e-mail
			final String SELECT_COUNT_QUERY = "SELECT COUNT(*) AS count FROM proprietaire WHERE email = ?";
			SelectQueryElement queryElement = (SelectQueryElement) new SelectQueryElement(SELECT_COUNT_QUERY)
					.setArgs(Map.of(1, email));

			// Exécution de la requête et récupération des résultats
			List<Map<String, Object>> results = (List<Map<String, Object>>) queryElement.execute();
			if (!results.isEmpty()) {
				// Récupération du champ "count" dans le premier résultat
				int count = (int) results.get(0).get("count");
				return count > 0;
			}

			return false;
		} catch (QueryElement.QueryException e) {
			throw new ProprietaireException("Erreur lors de la vérification de l'adresse e-mail : " + e.getMessage());
		}
	}


	// Classe interne pour les exceptions liées au propriétaire
	public static class ProprietaireException extends Exception {
		public ProprietaireException(String message) {
			super(message);
		}
	}


}
