package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.util.Unfinished;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Charges {

    private int idCharges;
    private float montant;
    private Date dateReleve;

    public Charges(Date dateReleve){
        this.dateReleve = dateReleve;
    }
    // Constructeur
    private Charges(int idCharges, Date dateReleve) {
        this.idCharges = idCharges;
        this.dateReleve = dateReleve;
    }

    // Getters et Setters

    @Unfinished
    public Bail getBail() {
        //TODO : query
        return null;
    }


    public int getIdCharges() {
        return idCharges;
    }

    public void setIdCharges(int idCharges) {
        this.idCharges = idCharges;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant des charges ne peut pas être négatif.");
        }
        this.montant = montant;
    }
    public static double calculerChargesProprietaire() throws Exception {
        final String SELECT_QUERY = """
        SELECT
            SUM(C.Montant) +
            SUM(IFNULL(CEau.PartieFixe + CEau.PartieVariable, 0)) +
            SUM(IFNULL(CEnt.Pourcentage * C.Montant / 100, 0)) +
            SUM(IFNULL(COM.Pourcentage * C.Montant / 100, 0)) AS TotalCharges
            FROM Charges C
            LEFT JOIN ChargesEau CEau ON C.IdCharges = CEau.IdCharges
            LEFT JOIN ChargesEntretien CEnt ON C.IdCharges = CEnt.IdCharges
            LEFT JOIN ChargesOrduresMenageres COM ON C.IdCharges = COM.IdCharges
            JOIN Bail B ON C.IdBail = B.IdBail
            WHERE B.Archive = FALSE
            """;

        double totalCharges = 0.0;

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();

            if (!result.isEmpty() && result.get(0).get("TotalCharges") != null) {
                totalCharges = (double) result.get(0).get("TotalCharges");
            }
        } catch (QueryElement.QEltException qEltException) {
            qEltException.getSqlException().printStackTrace();
            throw new Exception("Erreur lors du calcul des charges pour le propriétaire.", qEltException.getSqlException());
        }

        return totalCharges;
    }
    public static List<Charges> getChargesFromLocataire(Locataire locataire) throws ChargesException {
        List<Charges> chargesList = new LinkedList<>();
        final String SELECT_QUERY = """
        SELECT C.Montant 
        FROM Charges C
        JOIN Bail B ON C.IdBail = B.IdBail
        JOIN AssocieBailLocataire ABL ON B.IdBail = ABL.IdBail
        WHERE ABL.IdLocataire = ?""";

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.setArgs(Map.of(1, locataire.getIdLocataire())); // Assuming getId() retrieves the current Locataire's ID.
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();
            for (Map<String, Object> row : result) {
                //TODO : unfinished constructor de merde
                chargesList.add(new Charges((int) row.get("IdCharges"),(Date) row.get("DateCharge")));
            }
        } catch (QueryElement.QEltException qEltException) {
            qEltException.getSqlException().printStackTrace();
            throw new ChargesException("Erreur lors de la récupération des charges du locataire", qEltException.getSqlException());
        }
        return chargesList;
    }

    public Date getDateReleve() {
        return dateReleve;
    }

    public void setDateReleve(Date dateReleve) {
        this.dateReleve = dateReleve;
    }

    // Méthode pour valider la régularisation des charges
    public boolean validerRegularisationCharges(float montantVerse, float montantDu) {
        return Math.abs(montantVerse - montantDu) <= 0.01; // Tolérance d'arrondi
    }

    public static class ChargesException extends Queryable.QbleException {
        public ChargesException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }

}
