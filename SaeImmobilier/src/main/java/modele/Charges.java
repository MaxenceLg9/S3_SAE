package modele;

public class Charges {
    private Bail bail;
    private int IdCharges;
    private float Montant;
    private Date DateReleve;
    public Charges(int Idcharges) {
        this.IdCharges = IdCharges;
    }
    // Méthode pour valider la régularisation des charges
    public boolean validerRegularisationCharges(float montantVerse, float montantDu) {
        return Math.abs(montantVerse - montantDu) <= 0.01; // Tolérance d'arrondi
    }
}
