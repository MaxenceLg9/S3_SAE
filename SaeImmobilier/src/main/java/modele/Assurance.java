package modele;

public class Assurance {
    private float quotiteJurisprudence;
    private float protectionJuridique;
    private float prime;
    private String typeContrat;

    // Constructeur par défaut
    public Assurance() {}

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

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        if (typeContrat == null ||
                (!typeContrat.equalsIgnoreCase("propriétaire") &&
                        !typeContrat.equalsIgnoreCase("aide juridique"))) {
            throw new IllegalArgumentException("Le type de contrat doit être 'propriétaire' ou 'aide juridique'.");
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

    // Méthode pour valider la régularisation des charges
    public boolean validerRegularisationCharges(float montantVerse, float montantDu) {
        return Math.abs(montantVerse - montantDu) <= 0.01; // Tolérance d'arrondi
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
}
