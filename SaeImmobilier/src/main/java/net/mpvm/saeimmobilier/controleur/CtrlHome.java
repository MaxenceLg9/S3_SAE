package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.vue.VueBiens;
import net.mpvm.saeimmobilier.vue.VueLocataires;
import net.mpvm.saeimmobilier.vue.VueNewBien;
import net.mpvm.saeimmobilier.vue.VueNewLocataire;

public class CtrlHome {

    @FXML
    public void addBien(ActionEvent actionEvent){
        Stage stage = new Stage();
        try {
            VueNewBien.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void addLocataire(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueNewLocataire.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addAssurance(ActionEvent actionEvent) {
    }

    public void viewBien(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueBiens.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void vueLocataires(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueLocataires.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void vueAssurances(ActionEvent actionEvent) {
    }
}
