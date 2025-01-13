package net.mpvm.saeimmobilier.modele;


import java.sql.Date;

public class ChargeEau extends Charges{
    private int IdChargeEau;
    private int NouvelIndice;
    private float PartieFixe;
    private float PartieVariable;
    private int AncienIndice;

    private ChargeEau(Date DateReleve, int IdChargeEau){
        super(DateReleve);
        this.IdChargeEau = IdChargeEau;
    }
    public ChargeEau(Date DateReleve) {

        super(DateReleve);
    }
    public int getNouvelIndice() {

        return NouvelIndice;
    }

    public void setNouvelIndice(int NouvelIndice) throws IllegalArgumentException {
        if (NouvelIndice < AncienIndice) {
            throw new IllegalArgumentException("Le nouvel indice ne peut pas être inférieur à l'ancien.");
        }
        this.NouvelIndice = NouvelIndice;
    }

    public int getAncienIndice() {
        return AncienIndice;
    }

    public void setAncienIndice(int AncienIndice) {
        this.AncienIndice = AncienIndice;
    }

    public float getPartieFixe() {
        return PartieFixe;
    }

    public void setPartieFixe(float PartieFixe) {
        this.PartieFixe = PartieFixe;
    }
    public void mettreAJourIndice(int NouvelIndice) throws IllegalArgumentException {
        if (NouvelIndice < AncienIndice) {
            throw new IllegalArgumentException("Le nouvel indice ne peut pas être inférieur à l'ancien.");
        }
        this.AncienIndice = this.NouvelIndice;
        this.NouvelIndice = NouvelIndice;
    }
    public int getIdChargeEau() {
        return IdChargeEau;
    }
    public void setIdChargeEau(int IdChargeEau)  {
        this.IdChargeEau = IdChargeEau;
    }

    public float getPartieVariable() {
        return PartieVariable;
    }
    public void setPartieVariable(float PartieVariable) {
        this.PartieVariable = PartieVariable;
    }
}
