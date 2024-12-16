package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class Assurance implements Queryable{

    private int idAssurance;
    private int Annee;
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float prime;
    private TypeContrat typeContrat; // Type Propriétaire ou aide juridique, pour une des règles métier
    private float augmentationAnnuelle;
    private Optional<Bien> bien; // Bien lié à l'assurance
    private float primeAnneePrecedente;


    private Assurance(int IdAssurance,TypeContrat typeContrat){
        if (typeContrat == null) {
            throw new IllegalArgumentException("Le type de contrat est obligatoire.");
        }
        this.idAssurance = IdAssurance;
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
        if (typeContrat == null) {
            throw new IllegalArgumentException("Le type de contrat est obligatoire.");
        }
        this.typeContrat = typeContrat;
    }

    public Optional<Bien> getBien() {
        return bien;
    }

    public void setBien(Bien bien) {
        if (bien == null) {
            this.bien = Optional.empty(); // Supprime l'association avec un bien
            this.primeAnneePrecedente = 0; // Réinitialise la prime précédente
        } else {
            this.bien = Optional.of(bien);

            // Déterminer la prime de l'année précédente en fonction des données existantes
            Optional<Assurance> assurancePrecedente = bien.getAssuranceActuelle();
            if (assurancePrecedente.isPresent()) {
                this.primeAnneePrecedente = assurancePrecedente.get().getPrime();
            } else {
                this.primeAnneePrecedente = 0; // Aucune prime précédente si c'est une nouvelle assurance
            }
        }
    }

    // Méthode pour obtenir le montant de la quotité
    public float getMontantQuotite() {
        return this.quotiteJurisprudence;
    }

    // Méthode pour calculer le total de l'assurance
    public float TotalAssurance() {
        return this.quotiteJurisprudence + this.protectionJuridique + this.prime;
    }

    // Méthode pour revaloriser la prime
    public void revaloriserPrime(float pourcentageAugmentation) {
        if (pourcentageAugmentation < 0 || pourcentageAugmentation > 0.1) {
            throw new IllegalArgumentException("La revalorisation de la prime doit être comprise entre 0% et 10%.");
        }
        this.prime += this.prime * pourcentageAugmentation;
    }

    // Méthode pour valider la cohérence des montants calculés
    public boolean validerCoherenceAssurance() {
        float totalCalcule = this.TotalAssurance();
        if (totalCalcule < 0 || totalCalcule != (this.quotiteJurisprudence + this.protectionJuridique + this.prime)) {
            throw new IllegalStateException("Incohérence détectée dans les montants d'assurance.");
        }
        return true;
    }

    // Méthode pour calculer le pourcentage d'augmentation
    public float calculerPourcentageAugmentation(float primePrecedente, float primeActuelle) {
        if (primePrecedente <= 0) {
            throw new IllegalArgumentException("La prime précédente doit être supérieure à zéro.");
        }
        return ((primeActuelle - primePrecedente) / primePrecedente) * 100;
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
        // Valider les données avant insertion
        if (this.protectionJuridique < 0) {
            throw new AssuranceException("La protection juridique ne peut pas être négative.");
        }
        if (this.quotiteJurisprudence < 0) {
            throw new AssuranceException("La quotité juridique ne peut pas être négative.");
        }
        if (this.prime < 0) {
            throw new AssuranceException("La prime ne peut pas être négative.");
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

        } catch (QueryElement.QueryException e) {
            // Gérer les erreurs SQL et afficher des informations détaillées
            String errorMessage = String.format(
                    "Erreur lors de l'ajout de l'assurance : ProtectionJuridique=%f, QuotitéJuridique=%f, Prime=%f, TypeContrat=%s, Annee=%d",
                    this.protectionJuridique, this.quotiteJurisprudence, this.prime, this.typeContrat.toString(), this.Annee
            );
            throw new AssuranceException(errorMessage, e.getSqlException());
        }
    }

    @Override
    public void modify() throws QueryableException {

    }

    @Override
    public void delete() throws QueryableException {

    }

    public int getAnnee() {
        return Annee;
    }

    public void setAnnee(int annee) {
        Annee = annee;
    }

    public static class AssuranceException extends Queryable.QueryableException {
        public AssuranceException(String message){
            super(message);
        }
        public AssuranceException(String message, SQLException sqlException){
            super(message,sqlException);
        }
    }
}
