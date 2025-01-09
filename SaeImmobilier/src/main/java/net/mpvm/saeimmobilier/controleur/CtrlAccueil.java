package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

import static javafx.geometry.NodeOrientation.INHERIT;

public class CtrlAccueil {

    @FXML
    public TabPane tabPane;

    public void initialize(){
        refreshTable();
    }

    private void refreshTable() {
        tabPane.getTabs().forEach(tab -> {
            tab.setContent(new VBox(5));
            try {
                if("Immeubles".equals(tab.getText())){
                    Immeuble.findAll().forEach(immeuble -> {
                        ((VBox) tab.getContent()).getChildren().add(this.newElement(immeuble));
                    });
                } else if("Locataires".equals(tab.getText())){
                    Locataire.findAll().forEach(locataire -> {
                        ((VBox) tab.getContent()).getChildren().add(new Label(locataire.toString()));
                    });
                }
            } catch (Queryable.QbleException e) {
                JfxUtil.displayError("Erreur",e.getMessage());
            }
        });
    }

    private Node newElement(Immeuble i) {
        // Create the main container
        GridPane gP = new GridPane(10,10);

        gP.setHgap(10); // Horizontal gap between columns
        gP.setVgap(8);  // Vertical gap between rows
        gP.setPadding(new Insets(15)); // Padding inside the grid
        gP.setAlignment(Pos.CENTER); // Center-align content in the grid

        // Apply styling to the container
        gP.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #4a90e2, #007ec5);
        -fx-padding: 10px;
        -fx-border-color: #004c8c;
        -fx-border-width: 2px;
        -fx-border-radius: 5px;
        -fx-background-radius: 5px;
        -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0, 2, 2);"""
        );
        // Add hover effect
        gP.setOnMouseEntered(event -> gP.setStyle(gP.getStyle().replace("-fx-background-color: linear-gradient(to bottom, #4a90e2, #007ec5)", "-fx-background-color: #005ea2;")));
        gP.setOnMouseExited(event -> gP.setStyle(gP.getStyle().replace("-fx-background-color: #005ea2;", "-fx-background-color: linear-gradient(to bottom, #4a90e2, #007ec5)")));

        // Add content to the grid
        Label l1 = new Label("Adresse :" + i.getAdresse());
        Label l2 = new Label("Code Postal :" + i.getCodePostal());
        Label l3 = new Label("Ville :" + i.getVille());
        Label l4 = new Label("Nombre d'appartements :" + i.getNbAppartements(i.getIdBien()));
        gP.addColumn(0, l1,l2,l3,l4);

        // Center-align content in the grid
        ColumnConstraints column = new ColumnConstraints();
        column.setPrefWidth(Region.USE_COMPUTED_SIZE);
        column.setPercentWidth(-1);
        column.setHalignment(HPos.CENTER);
        gP.getColumnConstraints().addFirst(column);
        gP.getColumnConstraints().add(column);

        return gP;
    }

    @FXML
    public void ajouterBien(ActionEvent event){
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueNewBien.class);
        stage.getProperties().put("parent", this);
    }

    public void ajouterLocataire(ActionEvent event) {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueNewLocataire.class);
        stage.getProperties().put("parent", this);
    }

    public void vueImmeubles(ActionEvent event) {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage,VueImmeubles.class);
    }

    public void vueLocataires(ActionEvent event) {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage,VueLocataires.class);
    }

    public void gererLocation(ActionEvent actionEvent) {

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
    public void ajouterAssurance(ActionEvent event) {
        Stage stage = new Stage();
        JfxUtil.showWindow(stage, VueNewAssurance.class);
        stage.getProperties().put("parent", this);
    }
}
