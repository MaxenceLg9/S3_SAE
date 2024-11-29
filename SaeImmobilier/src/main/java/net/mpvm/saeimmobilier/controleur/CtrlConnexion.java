package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.awt.event.ActionEvent;

public class CtrlConnexion {
    @FXML
    private Label welcomeText;
    @FXML
    private Button BtwQuitter;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void Quitter(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void mdpOublie(javafx.event.ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "mdpoublie.fxml", "Modifier son mot de passe");
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}