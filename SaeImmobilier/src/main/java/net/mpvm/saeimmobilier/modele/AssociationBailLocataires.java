package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.util.Map;

public class AssociationBailLocataires {

    private final Locataire locataire;
    private final Bail bail;
    private final float partEau;
    private final float partLoyer;

    private float partElectricite;
    private float partEntretien;
    private float partOrduresMenageres;
    private Date dateEntree;
    private Date dateSortie;

    public AssociationBailLocataires(Locataire locataire, Bail bail, float partElectricite, float partEntretien, float partOrduresMenageres, float partEau, float partLoyer) {
        this.locataire = locataire;
        this.bail = bail;
        this.partElectricite = partElectricite;
        this.partEntretien = partEntretien;
        this.partOrduresMenageres = partOrduresMenageres;
        this.partEau = partEau;
        this.partLoyer = partLoyer;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float partElectricite, float partEntretien, float partOrduresMenageres, float partEau, float partLoyer, Date dateEntree) {
        this(locataire, bail, partElectricite, partEntretien, partOrduresMenageres, partEau, partLoyer);
        this.dateEntree = dateEntree;
    }

    public AssociationBailLocataires(Locataire locataire, Bail bail, float partElectricite, float partEntretien, float partOrduresMenageres, float partEau, float partLoyer, Date dateEntree, Date dateSortie) {
        this(locataire, bail, partElectricite, partEntretien, partOrduresMenageres, partEau, partLoyer, dateEntree);
        this.dateSortie = dateSortie;
    }

    public float getPartOrduresMenageres() {
        return partOrduresMenageres;
    }

    public static void delete(Bail bail) throws Queryable.QbleException {
        try (UpdateQueryElement updateQueryElement = new UpdateQueryElement("DELETE FROM AssocieBailLocataire WHERE IdBail = ?", true)) {
            updateQueryElement.setArgs(Map.of(1, bail.getIdBail()));
            updateQueryElement.execute();
        } catch (QueryElement.QEltException e) {
            throw new Queryable.QbleException("Erreur lors de la suppression des associations de bail", e.getSqlException());
        }
    }

        public Date getDateEntree() {
        return dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public float getPartEntretien() {
        return partEntretien;
    }

    public float getPartElectricite() {
        return partElectricite;
    }

    public Locataire getLocataire() {
        return locataire;
    }

    public Bail getBail() {
        return bail;
    }

    public float getPartLoyer() {
        return this.partLoyer;
    }

    public float getPartEau() {
        return partEau;
    }
}

