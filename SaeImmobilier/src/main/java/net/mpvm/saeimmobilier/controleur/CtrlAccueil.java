package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

public class CtrlAccueil {


    @FXML
    public void ajouterBien(ActionEvent event){
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueNewBien.class);
        stage.getProperties().put("parent", this);
    }



    public void vueImmeubles(ActionEvent event) {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage,VueImmeubles.class);
    }





    public void deconnexion(ActionEvent event) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            JfxUtil.showWindow(stage, VueConnexion.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void ajouterAssurance() {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueNewAssurance.class);
        stage.getProperties().put("parent", this);
    }
    @FXML
    public void Calculs() {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueCalculChargesEtImpots.class);
        stage.getProperties().put("parent", this);
    }
}
