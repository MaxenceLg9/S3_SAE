package net.mpvm.saeimmobilier.modele;

public class ChargeEntretien extends Charges {
    private int idChargeEntretien;
    private ChargeEntretien(ModeleDate dateReleve, int idChargeEntretien) {
        super(dateReleve);
        this.idChargeEntretien = idChargeEntretien;
    }
    // Constructeur
    public ChargeEntretien(ModeleDate dateReleve) {
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
