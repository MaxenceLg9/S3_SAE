package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Assurance extends Queryable{

    private int idAssurance;
    private int annee;
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float primePrecedente;
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

    public Assurance(ABuilder aBuilder) {
        super();
    }

    // Méthode pour récupérer toutes les assurances
    public static List<Assurance> findAll() throws AssuranceException {
        List<Assurance> assurances = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM Assurance";

        try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
            Result rs = query.execute();
            for(Map<String,Object> row : rs){
                Assurance assurance = new Assurance(
                        (int) row.get("IdAssurance"),
                        TypeContrat.valueOf((row.get("TypeContrat").toString()))
                );
                assurance.setAnnee((int) row.get("Annee"));
                assurance.setQuotiteJurisprudence((Float) row.get("QuotiteJuridique"));
                assurance.setProtectionJuridique((Float) row.get("ProtectionJuridique"));
                assurance.setPrime((Float) row.get("Prime"));
                assurance.setTotalPrime((Float) row.get("TotalPrime")); // Chargé depuis la base
                assurance.setMontantQuotite((Float) row.get("MontantQuotite")); // Calculé par trigger
                assurance.setAugmentationAnnuelle((Float) row.get("AugmentationAnnuelle")); // Calculé par trigger
                assurances.add(assurance);
            }
        } catch (QueryElement.QEltException qEltException) {
            throw new AssuranceException("Erreur lors de la récupération des assurances", qEltException.getSqlException());
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
        this.bien = Optional.of(bien);
    }

    public float getAugmentationAnnuelle() {
        return augmentationAnnuelle;
    }

    public void setAugmentationAnnuelle(float augmentationAnnuelle) {
        this.augmentationAnnuelle = augmentationAnnuelle;
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

        } catch (QueryElement.QEltException qEltException) {
            // Gestion d'une erreur SQL et affichage du contexte
            String errorMessage = String.format(
                    "Erreur lors de l'ajout de l'assurance : ProtectionJuridique=%f, QuotitéJuridique=%f, Prime=%f, TypeContrat=%s",
                    this.getProtectionJuridique(), this.getQuotiteJurisprudence(), this.getPrime(), this.getTypeContrat().toString()
            );
            throw new AssuranceException(errorMessage, qEltException.getSqlException());
        }
    }

    @Override
    public void modify() throws QbleException {
        update();
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }


    public void delete() throws AssuranceException {
        // Validation des données avant suppression
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide pour une suppression.");
        }
        if (this.annee <= 0) {
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
                    2, this.annee,
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

    @Override
    public void archiver() throws QbleException {

    }

    public void setBien(Optional<Bien> bien) {
        this.bien = bien;
    }

    public float getPrimePrecedente() {
        return primePrecedente;
    }

    public void setPrimePrecedente(float primePrecedente) {
        this.primePrecedente = primePrecedente;
    }

    protected void setId(int id) throws QbleException {

    }

    public int selectId() throws QbleException {
        return 0;
    }

    public Float getMontantQuotite() {
        return this.montantQuotite;
    }

    public void update() throws AssuranceException {
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide pour une mise à jour.");
        }

        String UPDATE_QUERY = """
        UPDATE Assurance SET
        ProtectionJuridique = ?,
        QuotitéJuridique = ?,
        Prime = ?,
        TypeContrat = ?,
        TotalPrime = ?
        WHERE IdAssurance = ?
        """;

        try (UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)) {
            query.setArgs(Map.of(
                    1, this.protectionJuridique,
                    2, this.quotiteJurisprudence,
                    3, this.prime,
                    4, this.typeContrat.toString(),
                    5, this.totalPrime,
                    6, this.idAssurance
            ));

            int rowsAffected = query.execute();
            if (rowsAffected == 0) {
                throw new AssuranceException("Aucune assurance correspondante trouvée pour la mise à jour.");
            }
            System.out.println("Mise à jour réussie pour l'assurance ID = " + this.idAssurance);
        } catch (QueryElement.QEltException e) {
            throw new AssuranceException("Erreur lors de la mise à jour de l'assurance avec ID " + this.idAssurance, e.getSqlException());
        }
    }

    public static class ABuilder extends Queryable.Builder{

        private int id;
        private TypeContrat typeContrat;
        private int annee;
        private float protectionJuridique;
        private float quotiteJuridique;
        private float prime;

        ABuilder(Map<String, Object> args) {
            this((int) args.get("IdAssurance"), TypeContrat.valueOf(args.get("TypeContrat").toString()), (int) args.get("Annee"), (float) args.get("ProtectionJuridique"), (float) args.get("QuotiteJuridique"), (float) args.get("Prime"));
        }

        private ABuilder(int id, TypeContrat typeContrat, int annee, float protectionJuridique, float quotiteJuridique, float prime) {
            this.id = id;
            this.typeContrat = typeContrat;
        }

        public ABuilder(TypeContrat typeContrat, int annee, float protectionJuridique, float quotiteJuridique, float prime) {
            this(-1,typeContrat, annee, protectionJuridique, quotiteJuridique, prime);
        }

        @Override
        public Assurance build() {
            return new Assurance(this);
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
