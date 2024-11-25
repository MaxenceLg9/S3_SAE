package net.mpvm.saeimmobilier.modele;

public enum TypeBien {
    BIEN_LOUABLE("Bien_Louable"),IMMEUBLE("Immeuble");

    private String designation;
    TypeBien(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return this.designation;
    }
}
