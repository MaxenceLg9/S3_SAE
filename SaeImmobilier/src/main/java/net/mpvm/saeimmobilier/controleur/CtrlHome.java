package net.mpvm.saeimmobilier.controleur;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

import java.time.LocalDate;
import java.util.List;

public class CtrlHome {

    @FXML
    public TabPane tabPane;

    public void initialize(){
        tabPane.getTabs().forEach(tab -> {
            tab.setContent(new VBox(5));
            try {
                if("Immeubles".equals(tab.getText())){
                    Immeuble.findAll().forEach(immeuble -> {
                        ((VBox) tab.getContent()).getChildren().add(new Label(immeuble.toString()));
                    });
                } else if("Locataires".equals(tab.getText())){
                    Locataire.findAll().forEach(locataire -> {
                        ((VBox) tab.getContent()).getChildren().add(new Label(locataire.toString()));
                    });
                } else if("Assurances".equals(tab.getText())){
                    Assurance.findAll().forEach(assurance -> {
                        ((VBox) tab.getContent()).getChildren().add(new Label(assurance.toString()));
                    });
                }
            } catch (Queryable.QbleException e) {
                JfxUtil.displayError("Erreur",e.getMessage());
            }
        });

    }

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
