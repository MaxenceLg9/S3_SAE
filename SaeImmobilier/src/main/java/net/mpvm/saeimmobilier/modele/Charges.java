package net.mpvm.saeimmobilier.modele;


public class Charges {
    private Bail bail;
    private int idCharges;
    private float montant;
    private ModeleDate dateReleve;
    public Charges(ModeleDate dateReleve){
        this.dateReleve = dateReleve;
    }
    // Constructeur
    private Charges(int idCharges, ModeleDate dateReleve) {
        this.idCharges = idCharges;
        this.dateReleve = dateReleve;
    }

    // Getters et Setters

    public Bail getBail() {
        return bail;
    }

    public void setBail(Bail bail) {
        this.bail = bail;
    }

    public int getIdCharges() {
        return idCharges;
    }

    public void setIdCharges(int idCharges) {
        this.idCharges = idCharges;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant des charges ne peut pas être négatif.");
        }
        this.montant = montant;
    }

    public ModeleDate getDateReleve() {
        return dateReleve;
    }

    public void setDateReleve(ModeleDate dateReleve) {
        this.dateReleve = dateReleve;
    }

    // Méthode pour valider la régularisation des charges
    public boolean validerRegularisationCharges(float montantVerse, float montantDu) {
        return Math.abs(montantVerse - montantDu) <= 0.01; // Tolérance d'arrondi
    }

}
