package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueConnexion;
import net.mpvm.saeimmobilier.vue.VueInscription;


public class CtrlAccueil {

    @FXML
    private Button btnConnexion;
    @FXML
    private Button btnInscription;

    public void Quitter(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void Inscription(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            VueInscription.showWindow(stage);

            Stage stage2 = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connexion(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            VueConnexion.showWindow(stage);

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
