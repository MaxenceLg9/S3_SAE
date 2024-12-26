package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

public class CtrlHome {

    @FXML
    public void ajouterBien(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueHome.class);


    }

    public void ajouterLocataire(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueHome.class);
    }

    public void ajouterAssurance(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueHome.class);
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
