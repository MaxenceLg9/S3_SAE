package net.mpvm.saeimmobilier.modele;

public class Habitation extends BienLouable {
    @Override
    public TypeBien getTypeBien() {
        return TypeBien.HABITATION;
    }


    public Habitation(String ville, int codePostal, String adresse, int nbPieces, int NumeroFiscal, Immeuble immeuble,float surface,Date dateAjout) {
        super(ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout);
    }
}
