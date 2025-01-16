package net.mpvm.saeimmobilier.controleur;

import com.sun.webkit.BackForwardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bail;
import net.mpvm.saeimmobilier.modele.Charges;
import net.mpvm.saeimmobilier.modele.Travaux;

import java.util.List;
import java.text.SimpleDateFormat;


public class CtrlCalculsChargesEtImpots {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private TextArea txtResultats;
    private Travaux travaux;
    private Bail bail;
    private Charges charges;
    private List<Travaux> travauxList;
    private List<Bail> bailsList;
    private List<Charges> chargesList;
    private GridPane gridPaneContent;

    @FXML
    private void calculerImpots() {
        try {
            txtResultats.setFont(Font.font("Calibri", FontWeight.BOLD, 16));

            double totalImpots = Travaux.calculerImpotsProprietaire();
            travauxList = Travaux.findAllCalculImpots();
            txtResultats.setText("");

            // Afficher les travaux dans le TextArea
            for (Travaux t : travauxList) {
                txtResultats.appendText(String.format("Devis Travaux : %s,  Montant à déclarer : %.2f €\n", t.getNumeroDevis(), t.getMontantADeclarer()));
            }
            if (totalImpots==0){
                txtResultats.appendText(String.format("Total des impôts pour le propriétaire : %.2f €\n", totalImpots));

            } else{
                txtResultats.appendText(String.format("\nTotal des impôts pour le propriétaire : %.2f €\n", totalImpots));

            }

        } catch (Exception e) {
            txtResultats.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    private void calculerLoyers() {
        try {
            txtResultats.setFont(Font.font("Calibri", FontWeight.BOLD, 16));

            double totalLoyers = Bail.calculerLoyersProprietaire();
            bailsList=Bail.findAllCalculLoyers();
            txtResultats.setText("");
            for (Bail b : bailsList) {
                txtResultats.appendText(String.format("Bail | Date Début : %s, Date Fin : %s, Loyer : %.2f €\n",
                        DATE_FORMAT.format(b.getDateDebut()), DATE_FORMAT.format(b.getDateFin()), b.getLoyer()));


            }
            if (totalLoyers==0){
                txtResultats.appendText(String.format("Total des loyers : %.2f €", totalLoyers));

            } else{
                txtResultats.appendText(String.format("\nTotal des loyers : %.2f €", totalLoyers));

            }
        } catch (Exception e) {
            txtResultats.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void calculerCharges() {
        try {
            txtResultats.setFont(Font.font("Calibri", FontWeight.BOLD, 16));
            txtResultats.setText("");

            double totalCharges = Charges.calculerChargesProprietaire();
            chargesList=Charges.getChargesDetails();
            for (Charges c : chargesList) {
                txtResultats.appendText(String.format("Date Charge : %s, Montant : %.2f €\n",
                        DATE_FORMAT.format(c.getDateReleve()), c.getMontant()));

            }
            if (totalCharges==0){
                txtResultats.appendText(String.format("Total des charges pour le propriétaire : %.2f €", totalCharges));

            } else{
                txtResultats.appendText(String.format("\nTotal des charges pour le propriétaire : %.2f €", totalCharges));
            }
        } catch (Exception e) {
            txtResultats.setText("Erreur : " + e.getMessage());
        }
    }
    @FXML
    private void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
