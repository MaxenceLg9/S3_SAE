package net.mpvm.saeimmobilier.modele;

import java.sql.Date;

public class ChargeElectricite extends Charges {

    private int idChargeElectricite;
    private ChargeElectricite(Date dateReleve, int idChargeElectricite) {
        super(dateReleve);
        this.idChargeElectricite = idChargeElectricite;
    }
    // Constructeur
    public ChargeElectricite(Date dateReleve) {
        super(dateReleve);
    }

    // Getters et Setters

    public int getIdChargeElectricite() {
        return idChargeElectricite;
    }

    public void setIdChargeElectricite(int idChargeElectricite) {
        this.idChargeElectricite = idChargeElectricite;
    }

}
