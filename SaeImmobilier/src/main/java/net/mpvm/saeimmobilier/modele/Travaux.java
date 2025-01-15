package net.mpvm.saeimmobilier.modele;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import net.mpvm.saeimmobilier.sql.Query.*;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class Travaux extends Queryable {
	private String numeroFacture;
	private String entreprise;
	private Float montant;
	private Float montantNonDeductible;
	private Float reduction;
	private String nature;
	private String numeroDevis;
	private Float montantADeclarer;
	private String entrepriseDevis;
	private Float montantDevis;
	private Date dateTravaux;
	private boolean recuperableImpots;
	private boolean recuperableLocataire;
	private int idTravaux;

	private Travaux(int idTravaux, String numeroFacture,String numeroDevis,Float montant,Float montantNonDeductible,Float reduction,String nature,String entreprise,Date dateTravaux){
		this.dateTravaux=dateTravaux;
		this.entreprise=entreprise;
		this.montant=montant;
		this.montantNonDeductible=montantNonDeductible;
		this.idTravaux=idTravaux;
		this.numeroDevis=numeroDevis;
		this.numeroFacture=numeroFacture;
		this.reduction=reduction;
		this.nature=nature;

	}

	private Travaux(TBuilder builder) {
		this(builder.IdTravaux, builder.numeroFacture, builder.numeroDevis, builder.montant,builder.montantNonDeductible,builder.reduction,builder.nature,builder.entreprise,builder.dateTravaux);
		this.numeroFacture = builder.numeroFacture;
		this.entreprise = builder.entreprise;
		this.montant = builder.montant;
		this.montantNonDeductible = builder.montantNonDeductible;
		this.reduction = builder.reduction;
		this.nature = builder.nature;
		this.numeroDevis = builder.numeroDevis;
		this.dateTravaux = builder.dateTravaux;


	}
	public static boolean numeroFactureExiste(String numeroFacture) {
		String query = "SELECT COUNT(*) as count FROM Travaux WHERE NumeroFacture = ?";
		try (SelectQueryElement sqlQuery = new SelectQueryElement(query)) {
			sqlQuery.setArgs(Map.of(1, numeroFacture));
			Result rs = sqlQuery.execute();
			if (!rs.isEmpty()) {
				Map<String, Object> row = rs.get(0); // Récupère la première ligne des résultats
				return ((Number) row.get("count")).intValue() > 0; // Vérifie si le compte est > 0
			}
			return false;
		} catch (QueryElement.QEltException e) {
			e.printStackTrace();
			return false; // Gère l'erreur en retournant `false`
		}
	}

	public static boolean numeroDevisExiste(String numeroDevis) {
		String query = "SELECT COUNT(*) as count FROM Travaux WHERE NumeroDevis = ?";
		try (SelectQueryElement sqlQuery = new SelectQueryElement(query)) {
			sqlQuery.setArgs(Map.of(1, numeroDevis));
			Result rs = sqlQuery.execute();
			if (!rs.isEmpty()) {
				Map<String, Object> row = rs.get(0); // Récupère la première ligne des résultats
				return ((Number) row.get("count")).intValue() > 0; // Vérifie si le compte est > 0
			}
			return false;
		} catch (QueryElement.QEltException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void save() throws TravauxException {
		// Vérification des champs obligatoires
		if (this.numeroFacture == null || this.numeroFacture.isEmpty()) {
			throw new TravauxException("Le numéro de facture est obligatoire pour sauvegarder un travail.");
		}
		if (this.entreprise == null || this.entreprise.isEmpty()) {
			throw new TravauxException("L'entreprise est obligatoire pour sauvegarder un travail.");
		}
		if (this.montant == null || this.montantNonDeductible == null || this.reduction == null) {
			throw new TravauxException("Les champs 'Montant', 'MontantNonDeductible' et 'Reduction' doivent être définis.");
		}
		if (this.montant < this.montantNonDeductible) {
			throw new TravauxException("Le montant non déductible ne peut pas être supérieur au montant total.");
		}
		if (this.reduction < 0 || this.reduction > 1) {
			throw new TravauxException("La réduction doit être comprise entre 0 et 1.");
		}

		// Calcul du montant à déclarer
		this.montantADeclarer = (this.montant - this.montantNonDeductible) * (1 - this.reduction);

		String INSERT_QUERY = """
        INSERT INTO Travaux (NumeroFacture, Entreprise, Montant, MontantNonDeductible, MontantADeclarer, Reduction, Nature, NumeroDevis, DateTravaux)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

		try (UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)) {
			query.setArgs(Map.of(
					1, this.numeroFacture,
					2, this.entreprise,
					3, this.montant,
					4, this.montantNonDeductible,
					5, this.montantADeclarer, // Insertion du montant à déclarer
					6, this.reduction,
					7, this.nature,
					8, this.numeroDevis,
					9, this.dateTravaux
			)).execute();

		} catch (QueryElement.QEltException qEltException) {
			throw new TravauxException(
					"Erreur lors de l'ajout des travaux : " + qEltException.getSqlException().getMessage(),
					qEltException.getSqlException()
			);
		}
	}


	public void setIdTravaux(int idTravaux) {
		this.idTravaux = idTravaux;
	}

	@Override
	public void modify() throws QbleException {

	}




	@Override
	public void archiver() throws QbleException {

	}
	public static List<Travaux> findAll() throws TravauxException {
		List<Travaux> travauxList = new ArrayList<>();
		String SELECT_QUERY = """
        SELECT IdTravaux,NumeroFacture, Entreprise, Montant, MontantNonDeductible, Reduction, Nature, NumeroDevis, DateTravaux
        FROM Travaux
    """;

		try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
			Result rs = query.execute();
			for (Map<String, Object> row : rs) {
				// Utilisation du builder directement depuis les données
				Travaux travaux = new Travaux.TBuilder(row).build();
				travauxList.add(travaux);
			}
		} catch (QueryElement.QEltException qEltException) {
			throw new TravauxException("Erreur lors de la récupération des travaux", qEltException.getSqlException());
		}

		return travauxList;
	}


	public static Travaux findById(String numeroFacture) throws TravauxException {
		String SELECT_QUERY = """
        SELECT NumeroFacture, Entreprise, Montant, MontantNonDeductible, Reduction, Nature, NumeroDevis, DateTravaux
        FROM Travaux
        WHERE NumeroFacture = ?
    """;

		try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
			query.setArgs(Map.of(1, numeroFacture));
			Result rs = query.execute();

			if (!rs.isEmpty()) {
				Map<String, Object> row = rs.get(0); // Assuming the first result is the one we need.
				return new Travaux.TBuilder(
						(int) row.get("IdTravaux"),
						(String) row.get("NumeroFacture"),
						(String) row.get("Entreprise"),
						(Date) row.get("DateTravaux"),
						(String) row.get("NumeroDevis"),
						(Float) row.get("Montant"),
						(Float) row.get("MontantNonDeductible"),
						(String) row.get("Nature"),
						(Float) row.get("Reduction")
						)
						.build();
			} else {
				throw new TravauxException("Aucun travail trouvé avec le numéro de facture : " + numeroFacture);
			}
		} catch (QueryElement.QEltException qEltException) {
			throw new TravauxException("Erreur lors de la récupération du travail", qEltException.getSqlException());
		}
	}

	public int getIdTravaux() {
		return this.idTravaux;
	}
	// Sélectionne l'ID du bien associé aux travaux
	public String selectIdBien() throws TravauxException {
		if (this.idTravaux <= 0) {
			throw new Travaux.TravauxException("L'ID des travaux est invalide.");
		}

		String SELECT_QUERY = "SELECT IdBien FROM Travaux WHERE IdTravaux = ?";
		try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
			query.setArgs(Map.of(1, this.idTravaux));
			Result rs = query.execute();

			if (rs.isEmpty()) {
				// Renvoie "aucun" si aucun résultat n'est trouvé
				return "aucun";
			}

			Map<String, Object> row = rs.get(0);
			return String.valueOf(row.get("IdBien"));
		} catch (QueryElement.QEltException qEltException) {
			throw new Travaux.TravauxException(
					"Erreur lors de la récupération de l'ID du bien associé aux Travaux avec ID " + this.idTravaux,
					qEltException.getSqlException()
			);
		}
	}

	// Méthode pour obtenir l'ID du bien d'un objet Travaux
	public String setIdBienTravaux(Travaux t) {
		try {
			return t.selectIdBien();  // Appelle la méthode selectIdBien()
		} catch (TravauxException e) {
			// Gestion plus précise de l'exception: on pourrait soit la relancer, soit retourner un message d'erreur
			System.err.println("Erreur lors de la récupération de l'ID du bien: " + e.getMessage());
			return "Aucun";
		}
	}

	@Override
	public void delete() throws Travaux.TravauxException {
		if (this.idTravaux <= 0) {
			throw new Travaux.TravauxException("L'ID des Travaux est invalide pour une suppression.");
		}

		String DELETE_QUERY = """
        DELETE FROM Travaux
        WHERE IdTravaux = ?
        """;

		try (UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)) {
			query.setArgs(Map.of(
					1, this.idTravaux
			));

			int rowsAffected = query.execute();
			if (rowsAffected == 0) {
				throw new Travaux.TravauxException("Aucuns travaux correspondants trouvés pour la suppression.");
			}
		} catch (QueryElement.QEltException e) {
			throw new Travaux.TravauxException("Erreur lors de la suppression des travaux avec ID " + this.idTravaux, e.getSqlException());
		}
	}
	public void attribuerDesTravaux(int idBien, int idTravaux) throws Travaux.TravauxException {
		if (idBien <= 0 || idTravaux <= 0) {
			throw new Travaux.TravauxException("Les ID du bien et des travaux doivent être valides. "+ idBien +"oui"+idTravaux);
		}

		String CHECK_QUERY = """
        SELECT COUNT(*) as count
        FROM Travaux
        WHERE IdBien = ? AND DateTravaux = ?
    	""";

		try (SelectQueryElement checkQuery = new SelectQueryElement(CHECK_QUERY)) {
			checkQuery.setArgs(Map.of(
					1, idBien,
					2, this.getDateTravaux()  // Assurez-vous que la méthode getDateTravaux() est bien définie
			));
			Result rs = checkQuery.execute();

			if (!rs.isEmpty()) {
				int count = ((Number) rs.get(0).get("count")).intValue();
				if (count > 0) {
					throw new Travaux.TravauxException(
							"Des travaux pour le bien avec la même date existent déjà. Association refusée."
					);
				}
			}
		} catch (QueryElement.QEltException e) {
			throw new Travaux.TravauxException(
					"Erreur lors de la vérification des travaux existants pour le bien avec ID " + idBien,
					e.getSqlException()
			);
		}

		// Attribution des travaux au bien
		String UPDATE_QUERY = """
        UPDATE Travaux
        SET IdBien = ?
        WHERE IdTravaux = ?
    """;

		try (UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)) {
			query.setArgs(Map.of(
					1, idBien,
					2, idTravaux
			));

			int rowsAffected = query.execute();
			if (rowsAffected == 0) {
				throw new Travaux.TravauxException("Aucun travail correspondant trouvé pour l'attribution.");
			}
		} catch (QueryElement.QEltException e) {
			throw new Travaux.TravauxException(
					"Erreur lors de l'attribution des travaux avec ID " + idTravaux + " au bien avec ID " + idBien,
					e.getSqlException()
			);
		}
	}


	public Date getDateTravaux() {
		return dateTravaux;
	}

	public void setDateTravaux(Date dateTravaux) {
		this.dateTravaux = dateTravaux;
	}

	public String getEntreprise() {
		return this.entreprise;
	}

	public Float getMontant() {
		return this.montant;
	}

	public Float getMontantNonDeductible() {
		return this.montantNonDeductible;
	}

	public String getNature() {
		return this.nature;
	}

	public String getNumeroDevis() {
		return this.numeroDevis;
	}

	public String getNumeroFacture() {
		return this.numeroFacture;
	}

	public Float getReduction() {
		return this.reduction;
	}

	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}

	public void setMontant(Float montant) {
		this.montant = montant;
	}

	public void setMontantNonDeductible(Float montantNonDeductible) {
		this.montantNonDeductible = montantNonDeductible;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public void setNumeroDevis(String numeroDevis) {
		this.numeroDevis = numeroDevis;
	}

	public void setNumeroFacture(String numeroFacture) {
		this.numeroFacture = numeroFacture;
	}

	public void setReduction(Float reduction) {
		this.reduction = reduction;
	}
	public Float getMontantADeclarer() {
		this.montantADeclarer=(this.montant-this.montantNonDeductible)*(1-this.reduction);
		return this.montantADeclarer;
	}


	public void setRecuperabilite(boolean impots, boolean locataire) throws IllegalArgumentException {
		if (impots && locataire) {
			throw new IllegalArgumentException("Une facture ne peut pas être récupérable aux impôts et au locataire simultanément.");
		}
		this.recuperableImpots = impots;
		this.recuperableLocataire = locataire;
	}

	public void associerDevis(String entreprise, Float montant) throws IllegalArgumentException {
		if (this.entrepriseDevis != null && !this.entrepriseDevis.equals(entreprise)) {
			throw new IllegalArgumentException("L'entreprise ne peut pas être modifiée.");
		}
		this.entrepriseDevis = entreprise;
		this.montantDevis = montant;
	}

	public boolean isRecuperableImpots() {
		return recuperableImpots;
	}

	public boolean isRecuperableLocataire() {
		return recuperableLocataire;
	}

	public Float getMontantDevis() {
		return montantDevis;
	}

	public String getEntrepriseDevis() {
		return entrepriseDevis;
	}
	public void setEntrepriseDevis(String entrepriseDevis) {
		this.entrepriseDevis = entrepriseDevis;
	}

	public void setMontantADeclarer(Float montantADeclarer) {
		this.montantADeclarer = montantADeclarer;
	}

	public void setMontantDevis(Float montantDevis) {
		this.montantDevis = montantDevis;
	}

	public void setRecuperableImpots(boolean recuperableImpots) {
		this.recuperableImpots = recuperableImpots;
	}
	public void setRecuperableLocataire(boolean recuperableLocataire) {
		this.recuperableLocataire = recuperableLocataire;
	}
	public static class TBuilder {
		private int IdTravaux;
		private String numeroFacture;
		private String entreprise;
		private Float montant;
		private Float montantNonDeductible;
		private Float reduction;
		private String nature;
		private String numeroDevis;
		private Date dateTravaux;

		public TBuilder(Map<String, Object> args) {
			this.IdTravaux = ((Number) args.get("IdTravaux")).intValue();
			this.numeroFacture = (String) args.get("NumeroFacture");
			this.entreprise = (String) args.get("Entreprise");
			this.montant = JfxUtil.doubleToFloat((Double) args.get("Montant"));
			this.montantNonDeductible = JfxUtil.doubleToFloat((Double) args.get("MontantNonDeductible"));
			this.reduction = JfxUtil.doubleToFloat((Double) args.get("Reduction"));
			this.nature = (String) args.get("Nature");
			this.numeroDevis = (String) args.get("NumeroDevis");
			this.dateTravaux = (Date) args.get("DateTravaux");
		}
		public TBuilder(int id,String numeroFacture, String entreprise, Date dateTravaux,String numeroDevis, Float montant,Float montantNonDeductible, String nature, Float reduction) {
			this.IdTravaux=id;
			this.numeroFacture = numeroFacture;
			this.dateTravaux = dateTravaux;
			this.entreprise = entreprise;
			this.numeroDevis = numeroDevis;
			this.montant = montant;
			this.montantNonDeductible = montantNonDeductible;
			this.nature = nature;
			this.reduction = reduction;
		}
		public TBuilder(String numeroFacture, String entreprise, Date dateTravaux,String numeroDevis, Float montant,Float montantNonDeductible, String nature, Float reduction) {
			this(-1, numeroFacture,entreprise,dateTravaux,numeroDevis,montant,montantNonDeductible,nature,reduction);
		}


		public Travaux build() {
			return new Travaux(this);
		}
	}

	public static class TravauxException extends Queryable.QbleException {
		public TravauxException(String message) {
			super(message);
		}

		public TravauxException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}
