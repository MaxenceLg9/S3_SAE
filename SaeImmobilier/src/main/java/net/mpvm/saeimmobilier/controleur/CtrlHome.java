package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

public class CtrlHome {

    @FXML
    public void ajouterBien(ActionEvent actionEvent){
        Stage stage = new Stage();
        try {
            VueNewBien.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void ajouterLocataire(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueNewLocataire.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void ajouterAssurance(ActionEvent actionEvent) {
        try {
            VueNewAssurance.showWindow(new Stage());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void vueImmeubles(ActionEvent actionEvent) {
        JfxUtil.showWindow(new Stage(),VueImmeubles.class);
        Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stageActu.close();

    }

    public void vueLocataires(ActionEvent actionEvent) {
        JfxUtil.showWindow(new Stage(),VueLocataires.class);
        Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stageActu.close();
    }


    public void gererLocation(ActionEvent actionEvent) {

    }
}
