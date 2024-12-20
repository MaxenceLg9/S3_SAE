package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Assurance extends Queryable{

    private int idAssurance;
    private int Annee;
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float prime;
    private TypeContrat typeContrat; // Type Propriétaire ou aide juridique
    private float augmentationAnnuelle; // Calculé en base
    private float montantQuotite; // Calculé en base
    private float totalPrime; // Calculé en base
    private Optional<Bien> bien; // Bien lié à l'assurance

    private Assurance(int idAssurance, TypeContrat typeContrat) {
        if (typeContrat == null) {
            throw new IllegalArgumentException("Le type de contrat est obligatoire.");
        }
        this.idAssurance = idAssurance;
        this.typeContrat = typeContrat;
        this.bien = Optional.empty(); // Initialisé à une valeur vide
    }

    // Constructeur par défaut
    public Assurance(TypeContrat typeContrat) {
        if (typeContrat == null) {
            throw new IllegalArgumentException("Le type de contrat est obligatoire.");
        }
        this.typeContrat = typeContrat;
        this.bien = Optional.empty(); // Initialisé à une valeur vide
    }

    // Méthode pour récupérer toutes les assurances
    public static List<Assurance> findAll() throws AssuranceException {
        List<Assurance> assurances = new ArrayList<>();
        String SELECT_QUERY = """
                SELECT IdAssurance, Annee, QuotiteJuridique, ProtectionJuridique, Prime, 
                       TotalPrime, MontantQuotite, AugmentationAnnuelle, TypeContrat 
                FROM Assurance
                """;

        try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
            ResultSet rs = query.execute();
            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getInt("IdAssurance"),
                        TypeContrat.valueOf(rs.getString("TypeContrat"))
                );
                assurance.setAnnee(rs.getInt("Annee"));
                assurance.setQuotiteJurisprudence(rs.getFloat("QuotiteJuridique"));
                assurance.setProtectionJuridique(rs.getFloat("ProtectionJuridique"));
                assurance.setPrime(rs.getFloat("Prime"));
                assurance.setTotalPrime(rs.getFloat("TotalPrime")); // Chargé depuis la base
                assurance.setMontantQuotite(rs.getFloat("MontantQuotite")); // Calculé par trigger
                assurance.setAugmentationAnnuelle(rs.getFloat("AugmentationAnnuelle")); // Calculé par trigger
                assurances.add(assurance);
            }
        } catch (QueryElement.QEltException | SQLException e) {
            throw new AssuranceException("Erreur lors de la récupération des assurances",
                    e instanceof SQLException ? (SQLException) e : ((QueryElement.QEltException) e).getSqlException());
        }

        return assurances;
    }


    public void setMontantQuotite(float montantQuotite) {
        this.montantQuotite = montantQuotite;
    }

    // Méthode pour obtenir le total de l'assurance
    public float getTotalPrime() {
        return totalPrime;
    }

    public void setTotalPrime(float totalPrime) {
        this.totalPrime = totalPrime;
    }

    // Méthode pour valider la cohérence des montants calculés

    // Méthode pour sauvegarder une assurance
    public void save() throws AssuranceException {
        // Valider les données avant insertion
        if (this.protectionJuridique < 0 || this.quotiteJurisprudence < 0 || this.prime < 0) {
            throw new AssuranceException("Les montants d'assurance ne peuvent pas être négatifs.");
        }
        if (this.typeContrat == null) {
            throw new AssuranceException("Le type de contrat est obligatoire.");
        }

        try (UpdateQueryElement query = new UpdateQueryElement(
                "INSERT INTO Assurance (ProtectionJuridique, QuotiteJuridique, Prime, TypeContrat, Annee) " +
                        "VALUES (?, ?, ?, ?, ?)",
                true)) {

            // Préparer les paramètres pour l'insertion
            query.setArgs(Map.of(
                    1, this.protectionJuridique,
                    2, this.quotiteJurisprudence,
                    3, this.prime,
                    4, this.typeContrat.toString(),
                    5, this.Annee
            ));

            // Exécution de la requête
            query.execute();
            System.out.println("Insertion réussie. Les triggers CalculTotalPrime et CalculPourcentageAugmentation sont déclenchés.");

        } catch (QueryElement.QEltException e) {
            // Gérer les erreurs SQL
            String errorMessage = String.format(
                    "Erreur lors de l'ajout de l'assurance : ProtectionJuridique=%f, QuotitéJuridique=%f, Prime=%f, TypeContrat=%s, Annee=%d",
                    this.protectionJuridique, this.quotiteJurisprudence, this.prime, this.typeContrat.toString(), this.Annee
            );
            throw new AssuranceException(errorMessage, e.getSqlException());
        }
    }

    // Getters et Setters

    public int getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(int idAssurance) {
        this.idAssurance = idAssurance;
    }

    public float getQuotiteJurisprudence() {
        return quotiteJurisprudence;
    }

    public void setQuotiteJurisprudence(float quotiteJurisprudence) {
        this.quotiteJurisprudence = quotiteJurisprudence;
    }

    public float getProtectionJuridique() {
        return protectionJuridique;
    }

    public void setProtectionJuridique(float protectionJuridique) {
        this.protectionJuridique = protectionJuridique;
    }

    public float getPrime() {
        return prime;
    }

    public void setPrime(float prime) {
        this.prime = prime;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Optional<Bien> getBien() {
        return bien;
    }

    public void setBien(Bien bien) {
        this.bien = Optional.ofNullable(bien);
    }

    public float getAugmentationAnnuelle() {
        return augmentationAnnuelle;
    }
    public void setAugmentationAnnuelle(float augmentationAnnuelle) {
        this.augmentationAnnuelle = augmentationAnnuelle;
    }

    public void setBien(Optional<Bien> bien) {
        this.bien = bien;
    }
    public void save() throws AssuranceException {
        // Valider les données de l'assurance avant l'insertion
        if (this.getProtectionJuridique() < 0) {
            throw new AssuranceException("La protection juridique ne peut pas être négative.");
        }
        if (this.getQuotiteJurisprudence() < 0) {
            throw new AssuranceException("La quotité juridique ne peut pas être négative.");
        }
        if (this.getPrime() < 0) {
            throw new AssuranceException("La prime ne peut pas être négative.");
        }
        if (this.getTypeContrat() == null) {
            throw new AssuranceException("Le type de contrat est obligatoire.");
        }

        try (UpdateQueryElement query = new UpdateQueryElement(
                "INSERT INTO Assurance (ProtectionJuridique, QuotitéJuridique, Prime, TypeContrat) VALUES (?, ?, ?, ?)",
                true)) {

            // Préparer les paramètres de la requête
            query.setArgs(Map.of(
                    1, this.getProtectionJuridique(),
                    2, this.getQuotiteJurisprudence(),
                    3, this.getPrime(),
                    4, this.getTypeContrat().toString()
            ));

            // Exécution de la requête
            query.execute();
            System.out.println("Insertion réussie. Le déclencheur CalculTotalPrime mettra à jour TotalPrime.");

        } catch (QueryElement.QEltException e) {
            // Gestion d'une erreur SQL et affichage du contexte
            String errorMessage = String.format(
                    "Erreur lors de l'ajout de l'assurance : ProtectionJuridique=%f, QuotitéJuridique=%f, Prime=%f, TypeContrat=%s",
                    this.getProtectionJuridique(), this.getQuotiteJurisprudence(), this.getPrime(), this.getTypeContrat().toString()
            );
            throw new AssuranceException(errorMessage, e.getSqlException());
        }
    }

    @Override
    public void modify() throws QueryableException {

    }

    public int getAnnee() {
        return Annee;
    }

    public void setAnnee(int annee) {
        this.Annee = annee;
    }


    public void delete() throws AssuranceException {
        // Validation des données avant suppression
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide pour une suppression.");
        }
        if (this.Annee <= 0) {
            throw new AssuranceException("L'année est obligatoire pour la suppression.");
        }
        if (this.typeContrat == null) {
            throw new AssuranceException("Le type de contrat est obligatoire pour la suppression.");
        }

        // Requête de suppression
        String DELETE_QUERY = """
        DELETE FROM Assurance
        WHERE IdAssurance = ? 
        AND Annee = ? 
        AND TypeContrat = ? 
        AND Prime = ? 
        AND QuotiteJuridique = ? 
        AND ProtectionJuridique = ?
        """;

        try (UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)) {
            // Préparation des paramètres de la requête
            query.setArgs(Map.of(
                    1, this.idAssurance,
                    2, this.Annee,
                    3, this.typeContrat.toString(),
                    4, this.prime,
                    5, this.quotiteJurisprudence,
                    6, this.protectionJuridique
            ));

            // Exécution de la requête
            int rowsAffected = query.execute();
            if (rowsAffected == 0) {
                throw new AssuranceException("Aucune assurance correspondante trouvée pour la suppression.");
            }
            System.out.println("Assurance supprimée avec succès : ID = " + this.idAssurance);
        } catch (QueryElement.QEltException e) {
            throw new AssuranceException("Erreur lors de la suppression de l'assurance avec ID " + this.idAssurance, e.getSqlException());
        }
    }

    protected void setId(int id) throws QbleException {

    }

    public int selectId() throws QbleException {
        return 0;
    }

    public static class ABuilder extends Queryable.Builder{

        ABuilder() {

        }

        @Override
        public Assurance build() {
            return new Assurance(1,TypeContrat.AIDE_JURIDIQUE);
        }
    }

    public static class AssuranceException extends Queryable.QbleException {
        public AssuranceException(String message){
            super(message);
        }

        public AssuranceException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}
