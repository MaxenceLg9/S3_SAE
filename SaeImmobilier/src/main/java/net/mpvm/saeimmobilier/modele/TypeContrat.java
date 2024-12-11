package net.mpvm.saeimmobilier.modele;

public enum TypeContrat {
    PROPRIETAIRE ,
    AIDE_JURIDIQUE;



    @Override
    public String toString() {
        if (this == AIDE_JURIDIQUE){
            return "AIDE_JURIDIQUE";
        } else {
            return "PROPRIETAIRE";
        }

    }
}
