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


public class CtrlCalculsChargesEtImpots {

    @FXML
    private TextArea txtResultats;
    private Travaux travaux;
    private Bail bail;
    private Charges charges;
    private List<Travaux> travauxList;
    private GridPane gridPaneContent;

    @FXML
    private void calculerImpots() {
        try {
            double totalImpots = travaux.calculerImpotsProprietaire();
            travauxList = travaux.findAllCalculImpots();
            txtResultats.setText("");
            // Définir la police Calibri, taille 14px pour les travaux
            txtResultats.setFont(Font.font("Calibri", FontWeight.NORMAL, 14));

            // Afficher les travaux dans le TextArea
            for (Travaux t : travauxList) {
                txtResultats.appendText(String.format("Devis Travaux : %s,  Montant à déclarer : %.2f €\n", t.getNumeroDevis(), t.getMontantADeclarer()));
            }

            // Appliquer une police plus grande et en gras pour le total des impôts
            txtResultats.setFont(Font.font("Calibri", FontWeight.BOLD, 16));
            txtResultats.appendText(String.format("\nTotal des impôts pour le propriétaire : %.2f €\n", totalImpots));

        } catch (Exception e) {
            txtResultats.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    private void calculerLoyers() {
        try {
            double totalLoyers = bail.calculerLoyersProprietaire();
            txtResultats.setText(String.format("Total des loyers : %.2f €", totalLoyers));
        } catch (Exception e) {
            txtResultats.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void calculerCharges() {
        try {
            double totalCharges = charges.calculerChargesProprietaire();
            txtResultats.setText(String.format("Total des charges pour le propriétaire : %.2f €", totalCharges));
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
