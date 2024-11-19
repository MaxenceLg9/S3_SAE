package modele;

import java.util.Date;

public class Paiement {
    private float montant;
    private Date datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statutPaiement;

    // Constructeur privé qui demande tous les attributs
    private Paiement(float montant, Date datePaiement, TypePaiement typePaiement, StatutPaiement statutPaiement) {
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statutPaiement = statutPaiement;
    }

    // Constructeur public qui demande tous les attributs sauf statutPaiement
    public Paiement(float montant, Date datePaiement, TypePaiement typePaiement) {
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
    }

    // Getters
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
}
