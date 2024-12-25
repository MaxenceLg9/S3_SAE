package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueConnexion;
import net.mpvm.saeimmobilier.vue.VueInscription;


public class CtrlAccueil {

    @FXML
    private Button btnDefault;

    public void initialize() {
        try {
            toggleButton(Proprietaire.countProprietaire() == 0);
        } catch (Proprietaire.ProprietaireException e) {
            JfxUtil.displayError("Erreur de récupération des données", "L'application n'a pas pu se lancer. Vérifiez votre connexion à la base de données.");
            throw new RuntimeException();
        }
        // Initialisation
    }

    public void toggleButton(boolean toggle) {
        if(toggle) {
            btnDefault.setOnAction(this::Inscription);
            btnDefault.setText("Inscription");
            btnDefault.setStyle("-fx-background-color: #2ba530;" + btnDefault.getStyle());
        }
        else {
            btnDefault.setOnAction(this::Connexion);
            btnDefault.setText("Connexion");
            btnDefault.setStyle("-fx-background-color: #088791;-fx-background-radius: 20px;" + btnDefault.getStyle());
        }
    }

    public void Quitter() {
        System.exit(0);
    }

    public void Inscription(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.showWindow(stage, VueInscription.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Connexion(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.showWindow(stage, VueConnexion.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
