package modele;

import java.util.Optional;

public class Assurance {
    private int IdAssurance;
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float prime;
    private TypeContrat typeContrat; // Type Propriétaire ou aide juridique, pour une des règles métier
    private float AugmentationAnnuelle;
    private Optional<Bien> bien;

    // Constructeur par défaut
    public Assurance(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    // Getters et Setters
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

    // Méthode getMontantQuotité
    public float getMontantQuotite() {
        return this.quotiteJurisprudence;
    }

    // Méthode TotalAssurance
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
}
