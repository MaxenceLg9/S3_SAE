package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

public class CtrlHome {

    @FXML
    public void ajouterBien(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueNewBien.class);
    }

    public void ajouterLocataire(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueNewLocataire.class);
    }

    public void ajouterAssurance(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueNewAssurance.class);
    }

    public void vueImmeubles(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage,VueImmeubles.class);

    }

    public void vueLocataires(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage,VueLocataires.class);
    }


    public void gererLocation(ActionEvent event) {

    }
}
