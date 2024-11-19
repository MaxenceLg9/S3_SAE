package net.mpvm.saeimmobilier.modele;

public class ChargeElectricite extends Charges {
    private int idChargeElectricite;

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
