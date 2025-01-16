package net.mpvm.saeimmobilier.modele;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class AssociationBailLocataires {

    private final Locataire locataire;
    private final Bail bail;

    private float repartitionElectricite;
    private float repartitionEntretien;
    private float repartitionOrduresMenageres;
    private Date dateEntree;
    private Date dateSortie;

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres) {
        this.locataire = locataire;
        this.bail = bail;
        this.repartitionElectricite = repartitionElectricite;
        this.repartitionEntretien = repartitionEntretien;
        this.repartitionOrduresMenageres = repartitionOrduresMenageres;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres, Date dateEntree) {
        this(locataire, bail, repartitionElectricite, repartitionEntretien, repartitionOrduresMenageres);
        this.dateEntree = dateEntree;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float repartitionElectricite, float repartitionEntretien, float repartitionOrduresMenageres, Date dateEntree, Date dateSortie) {
        this(locataire, bail, repartitionElectricite, repartitionEntretien, repartitionOrduresMenageres, dateEntree);
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
}

