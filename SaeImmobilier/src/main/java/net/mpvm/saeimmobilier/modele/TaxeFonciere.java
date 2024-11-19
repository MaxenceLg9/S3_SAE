package net.mpvm.saeimmobilier.modele;

public class TaxeFonciere extends Document{
    private float Montant;
    private int Annee;
    private int idTaxeFonciere;
    private TaxeFonciere(String cheminDocument,Date DateAjout,int idTaxeFonciere,int Annee){
        super(cheminDocument,DateAjout);
        this.idTaxeFonciere = idTaxeFonciere;
        this.Annee = Annee;
    }
    public TaxeFonciere(String cheminDocument, Date dateAjout,int Annee) {
        super(cheminDocument, dateAjout);
        this.Annee = Annee;
    }
}
