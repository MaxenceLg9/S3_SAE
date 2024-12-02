package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;


public class CtrlAccueil {

    @FXML
    private Button btnConnexion;
    @FXML
    private Button btnInscription;

    public void Quitter(ActionEvent actionEvent) {
    }

    public void Inscription(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "connexion.fxml", "Connexion");

            Stage stage2 = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connexion(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "connexion.fxml", "Connexion");

            Stage stage2 = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
