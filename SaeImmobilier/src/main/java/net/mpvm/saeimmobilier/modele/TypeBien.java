package net.mpvm.saeimmobilier.modele;

public enum TypeBien {
    HABITATION(Habitation.class),GARAGE(Garage.class), IMMEUBLE(Immeuble.class);

    private final Class<? extends Bien> tClass;

    TypeBien(Class<? extends Bien> tClass) {
        this.tClass = tClass;
    }

    public Class<? extends Bien> getTClass() {
        return tClass;
    }
}
