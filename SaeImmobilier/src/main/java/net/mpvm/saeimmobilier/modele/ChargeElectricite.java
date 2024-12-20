package net.mpvm.saeimmobilier.modele;

public class ChargeElectricite extends Charges {
    private int idChargeElectricite;
    private ChargeElectricite(ModeleDate dateReleve, int idChargeElectricite) {
        super(dateReleve);
        this.idChargeElectricite = idChargeElectricite;
    }
    // Constructeur
    public ChargeElectricite(ModeleDate dateReleve) {
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
