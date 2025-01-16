package net.mpvm.saeimmobilier.modele;

import java.sql.Date;

public class AssociationBailLocataires {

    private final Locataire locataire;
    private final Bail bail;
    private final float partEau;
    private final float repartitionLoyer;

    private float repartitionElectricite;
    private float repartitionEntretien;
    private float repartitionOrduresMenageres;
    private Date dateEntree;
    private Date dateSortie;

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres, float partEau, float repartitionLoyer) {
        this.locataire = locataire;
        this.bail = bail;
        this.repartitionElectricite = repartitionElectricite;
        this.repartitionEntretien = repartitionEntretien;
        this.repartitionOrduresMenageres = repartitionOrduresMenageres;
        this.partEau = partEau;
        this.repartitionLoyer = repartitionLoyer;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres, float partEau, float repartitionLoyer, Date dateEntree) {
        this(locataire, bail, repartitionElectricite, repartitionEntretien, repartitionOrduresMenageres, partEau, repartitionLoyer);
        this.dateEntree = dateEntree;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres, float partEau, float repartitionLoyer, Date dateEntree, Date dateSortie) {
        this(locataire, bail, repartitionElectricite, repartitionEntretien, repartitionOrduresMenageres, partEau, repartitionLoyer, dateEntree);
        this.dateSortie = dateSortie;
    }

    public float getRepartitionOrduresMenageres() {
        return repartitionOrduresMenageres;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public float getRepartitionEntretien() {
        return repartitionEntretien;
    }

    public float getRepartitionElectricite() {
        return repartitionElectricite;
    }

    public Locataire getLocataire() {
        return locataire;
    }

    public Bail getBail() {
        return bail;
    }

    public float getRepartitionLoyer() {
        return this.repartitionLoyer;
    }

    public float getPartEau() {
        return partEau;
    }
}

