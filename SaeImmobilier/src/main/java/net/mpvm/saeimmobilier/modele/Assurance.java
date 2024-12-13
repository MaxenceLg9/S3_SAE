package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class Assurance {

    private int idAssurance;
    private int Annee;
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float prime;
    private TypeContrat typeContrat; // Type Propriétaire ou aide juridique, pour une des règles métier
    private float augmentationAnnuelle;
    private Optional<Bien> bien; // Bien lié à l'assurance


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
        this.bien = Optional.ofNullable(bien); // Permet de lier ou de supprimer le bien
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

        } catch (QueryElement.QueryException e) {
            // Gestion d'une erreur SQL et affichage du contexte
            String errorMessage = String.format(
                    "Erreur lors de l'ajout de l'assurance : ProtectionJuridique=%f, QuotitéJuridique=%f, Prime=%f, TypeContrat=%s",
                    this.getProtectionJuridique(), this.getQuotiteJurisprudence(), this.getPrime(), this.getTypeContrat().toString()
            );
            throw new AssuranceException(errorMessage, e.getSqlException());
        }
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
