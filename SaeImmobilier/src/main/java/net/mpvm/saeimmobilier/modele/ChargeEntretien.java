package net.mpvm.saeimmobilier.modele;

import java.sql.Date;

public class ChargeEntretien extends Charges {
    private int idChargeEntretien;

    private ChargeEntretien(Date dateReleve, int idChargeEntretien) {
        super(dateReleve);
        this.idChargeEntretien = idChargeEntretien;
    }
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
