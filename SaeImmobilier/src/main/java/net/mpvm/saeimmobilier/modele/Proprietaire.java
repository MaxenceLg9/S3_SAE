package net.mpvm.saeimmobilier.modele;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.mpvm.saeimmobilier.sql.Query.*;

public class Proprietaire extends Queryable{
	public static final String INSERT_QUERY = "INSERT INTO proprietaire (Email,MotDePasse) VALUES (?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM proprietaire";
	public static final String DELETE_QUERY = "DELETE FROM proprietaire WHERE IdProprietaire = ?";
	public static final String SELECT_COUNT_WHERE_EMAIL = "SELECT COUNT(*) AS count FROM proprietaire WHERE email = ?";
	public static final String SELECT_COUNT_PROPRIETAIRE = "SELECT COUNT(*) AS count FROM proprietaire";


	private String email;
	private String password;
	private ArrayList<Bien> biensPossedes;
	private int idProprietaire;

	private Proprietaire(int idProprietaire, String email, String password) throws ProprietaireException {
		setEmail(email);
		setPassword(password);
		this.idProprietaire = idProprietaire;
		this.biensPossedes = new ArrayList<>();
	}

	public Proprietaire(String Email, String password) throws ProprietaireException {
		this(-1, Email, password);
	}

	// Getters et setters pour les propriétaires

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}


	public void setEmail(String email) throws ProprietaireException {
		if (!checkEmail(email)) {
			throw new ProprietaireException("L'adresse e-mail saisie n'est pas conforme");
		}
		this.email = email;
	}

	public void setPassword(String password) throws ProprietaireException {
		if (!checkPassword(password)) {
			throw new ProprietaireException("Le mot de passe doit faire 8 caractères, contenir une majuscule, une minuscule, un chiffre et un caractère spécial au minimum");
		}
		this.password = password;
	}

	public static long countProprietaire() throws ProprietaireException {
		try(SelectQueryElement query = new SelectQueryElement(SELECT_COUNT_PROPRIETAIRE)){
			Result rs = query.execute();
			return (long) rs.getFirst().get("count");
		}
		catch (QueryElement.QEltException qEltException){
			throw new ProprietaireException("Erreur lors de la récupération du nombre de propriétaires");
		}
	}

	private boolean checkPassword(String password){
		return Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$", password);
	}

	private boolean checkEmail(String email){
		return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
	}


	public boolean verifierMontantRegularisation(float sommeVersee, float sommeDue) {
		return Math.abs(sommeVersee - sommeDue) < 0.01; // Tolérance pour arrondis
	}

	public int getIdProprietaire() {
		return idProprietaire;
	}

	public void setIdProprietaire(int idProprietaire) {
		this.idProprietaire = idProprietaire;
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
									2, this.getPassword()
							))
					.execute();
		} catch (QueryElement.QEltException QEltException) {
			throw new ProprietaireException("Erreur lors de l'ajout du propriétaire : ", QEltException.getSqlException());
		}
	}

	@Override
	public void modify() throws QbleException {

	}

	@Override
	public void delete() throws QbleException {

	}

	@Override
	public void archiver() throws QbleException {

	}

	private boolean emailAlreadyExists(String email) throws ProprietaireException {
		try(SelectQueryElement queryElement = new SelectQueryElement(SELECT_COUNT_WHERE_EMAIL)) {
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