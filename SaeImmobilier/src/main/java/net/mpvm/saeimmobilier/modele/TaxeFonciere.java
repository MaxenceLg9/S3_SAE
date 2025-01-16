package net.mpvm.saeimmobilier.modele;


import java.sql.Date;

public class TaxeFonciere extends Document{
    private float Montant;
    private int Annee;
    private int idTaxeFonciere;
    private TaxeFonciere(String cheminDocument, Date DateAjout, int idTaxeFonciere, int Annee){
        super(cheminDocument,DateAjout);
        this.idTaxeFonciere = idTaxeFonciere;
        this.Annee = Annee;
    }
    public TaxeFonciere(String cheminDocument, Date dateAjout, int Annee) {
        super(cheminDocument, dateAjout);
        this.Annee = Annee;
    }

    public int getIdTaxeFonciere() {
        return idTaxeFonciere;
    }
    public void setIdTaxeFonciere(int idTaxeFonciere) {
        this.idTaxeFonciere = idTaxeFonciere;
    }

    public float getMontant() {
        return Montant;
    }
    public void setMontant(float montant) {
        Montant = montant;
    }
    public int getAnnee() {
        return Annee;
    }
    public void setAnnee(int annee) {
        Annee = annee;
    }
}
