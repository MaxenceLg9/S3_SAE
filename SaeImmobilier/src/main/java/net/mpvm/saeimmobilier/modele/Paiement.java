package net.mpvm.saeimmobilier.modele;

import java.util.ArrayList;

public class Paiement {
    private int idPaiement;  // Attribut unique non demandé dans le constructeur public
    private float montant;
    private Date datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statutPaiement;
    private Bail bail;

    // Constructeur privé qui demande tous les attributs, y compris idPaiement
    private Paiement(int idPaiement, float montant, Date datePaiement, TypePaiement typePaiement, StatutPaiement statutPaiement) {
        this.idPaiement = idPaiement;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statutPaiement = statutPaiement;
    }

    // Constructeur public qui demande tous les attributs sauf idPaiement
    public Paiement(float montant, Date datePaiement, TypePaiement typePaiement, StatutPaiement statutPaiement) {
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statutPaiement = statutPaiement;
    }

    // Getters
    public int getIdPaiement() {
        return idPaiement;
    }

    public float getMontant() {
        return montant;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    // Setters
    public void setMontant(float montant) {
        this.montant = montant;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
    public void setBail(Bail bail) {
        this.bail = bail;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Bail getBail() {
        return bail;
    }

}
