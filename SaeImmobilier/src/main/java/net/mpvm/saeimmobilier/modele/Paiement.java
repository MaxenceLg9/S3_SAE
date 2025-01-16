package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Paiement {
    private int idPaiement;  // Attribut unique non demandé dans le constructeur public
    private float montant;
    private Date datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statutPaiement;
    private Bail bail;

    // Constructeur privé qui demande tous les attributs, y compris idPaiement
    private Paiement(int idPaiement, float montant, Date datePaiement, TypePaiement typePaiement, StatutPaiement statutPaiement) {
        this.idPaiement = idPaiement;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statutPaiement = statutPaiement;
    }

    // Constructeur public qui demande tous les attributs sauf idPaiement
    public Paiement(float montant, Date datePaiement, TypePaiement typePaiement, StatutPaiement statutPaiement) {
        this(-1, montant, datePaiement, typePaiement, statutPaiement);
    }

    public Paiement(Map<String, Object> row) {
        this(
                (int) row.get("IdPaiement"),
                (float) row.get("Montant"),
                (Date) row.get("DatePaiement"),
                TypePaiement.valueOf((String) row.get("TypePaiement")),
                StatutPaiement.valueOf((String) row.get("StatutPaiement"))
        );
    }

    // Getters
    public int getIdPaiement() {
        return idPaiement;
    }

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

    public void setBail(Bail bail) {
        this.bail = bail;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Bail getBail() {
        return bail;
    }

    public static List<Paiement> getPaiements(Bail bail) throws PaiementException {
        List<Paiement> paiements = new LinkedList<>();
        final String SELECT_QUERY = "SELECT * FROM Paiement WHERE IdBail = ?";

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.setArgs(Map.of(1, bail.getIdBail()));
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();
            for (Map<String, Object> row : result) {
                paiements.add(new Paiement(row));
            }
        } catch (QueryElement.QEltException qEltException) {
            qEltException.getSqlException().printStackTrace();
            throw new PaiementException("Erreur lors de la récupération des paiements", qEltException.getSqlException());
        }
        return paiements;
    }

    public static class PaiementException extends QueryElement.QEltException{

        public PaiementException(String message) {
            this(message,null);
        }

        public PaiementException(String message, SQLException sqlException){
            super(message, sqlException);
        }
    }
}
