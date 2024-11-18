package modele;

public class Charges {
    private Bail bail;
    private int IdCharge;
    private float Montant;
    private Date DateReleve;
    public Charges(Bail bail) {
        this.bail = bail;
    }
}
