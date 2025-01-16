package net.mpvm.saeimmobilier.modele;


import java.sql.Date;

public class Quittance extends Document {
    private float montantLoyer;
    private float montantCharges;
    private Paiement paiement;
    private int IdQuittance;
    // Constructeur privé qui demande tous les attributs (y compris ceux de la classe parente)
    private Quittance(String cheminDocument, Date dateAjout, int IdQuittance, float montantLoyer, float montantCharges) {
        super(cheminDocument, dateAjout); // Appelle le constructeur de la classe parente
        this.montantLoyer = montantLoyer;
        this.montantCharges = montantCharges;
        this.IdQuittance = IdQuittance;
    }

    // Constructeur public qui demande tous les attributs sauf ceux de la classe parente
    public Quittance(String cheminDocument, Date dateAjout, float montantLoyer, float montantCharges) {
        super(cheminDocument, dateAjout); // Appelle le constructeur de la classe parente
        this.montantLoyer = montantLoyer;
        this.montantCharges = montantCharges;

    }

    // Getters
    public float getMontantLoyer() {
        return montantLoyer;
    }

    public float getMontantCharges() {
        return montantCharges;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    // Setters
    public void setMontantLoyer(float montantLoyer) {
        this.montantLoyer = montantLoyer;
    }

    public void setMontantCharges(float montantCharges) {
        this.montantCharges = montantCharges;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public int getIdQuittance() {
        return IdQuittance;
    }
    public void setIdQuittance(int IdQuittance) {
        this.IdQuittance = IdQuittance;
    }
}
