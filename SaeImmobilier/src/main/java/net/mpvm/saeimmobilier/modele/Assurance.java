package net.mpvm.saeimmobilier.modele;


import java.util.Optional;

public class Assurance {
    private int idAssurance;
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
}
