package modele;

public class ChargeEntretien extends Charges {
    private int idChargeEntretien;

    // Constructeur
    public ChargeEntretien(Date dateReleve) {
        super(dateReleve);
    }

    // Getters et Setters

    public int getIdChargeEntretien() {
        return idChargeEntretien;
    }

    public void setIdChargeEntretien(int idChargeEntretien) {
        this.idChargeEntretien = idChargeEntretien;
    }
}
