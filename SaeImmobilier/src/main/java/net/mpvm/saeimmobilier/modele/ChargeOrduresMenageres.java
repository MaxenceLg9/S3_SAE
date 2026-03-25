package net.mpvm.saeimmobilier.modele;

public class ChargeOrduresMenageres extends Charges {
    private int idChargesOrduresMenageres;

    private ChargeOrduresMenageres(ModeleDate dateReleve, int IdChargesOrduresMenageres) {
        super(dateReleve);
        this.idChargesOrduresMenageres = IdChargesOrduresMenageres;
    }
    public ChargeOrduresMenageres(ModeleDate dateReleve) {
        super(dateReleve);
    }

    // Getters et Setters

    public int getIdChargesOrduresMenageres() {
        return idChargesOrduresMenageres;
    }

    public void setIdChargesOrduresMenageres(int idChargesOrduresMenageres) {
        this.idChargesOrduresMenageres = idChargesOrduresMenageres;
    }
}