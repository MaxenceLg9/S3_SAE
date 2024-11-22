package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.mpvm.saeimmobilier.modele.Locataire;

import java.awt.desktop.SystemEventListener;

public class CtrlViewLocataires {
    @FXML
    public VBox vBoxContent;

    public void initialize(){
        for(Locataire l : Locataire.getLocataires()) {
            GridPane gp = new GridPane(1, 1);
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(100);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES); // 25% for column 1

            gp.getColumnConstraints().addAll(col1,col1);

            Label nom = new Label(l.getNom());
            Label prenom = new Label(l.getPrenom());
            Label email = new Label(l.getEmail());
            Label telephone = new Label(l.getTelepone());

            gp.add(nom,0,0);
            gp.add(prenom,0,1);
            gp.add(email,1,1);
            gp.add(telephone,1,0);

            gp.setAlignment(Pos.TOP_CENTER);

            GridPane.setHalignment(nom, HPos.LEFT);
            GridPane.setHalignment(prenom, HPos.LEFT);
            GridPane.setHalignment(email, HPos.LEFT);
            GridPane.setHalignment(telephone, HPos.LEFT);
            GridPane.setValignment(nom, VPos.CENTER);
            GridPane.setValignment(prenom, VPos.CENTER);
            GridPane.setValignment(email, VPos.CENTER);
            GridPane.setValignment(telephone, VPos.CENTER);

            gp.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");
            gp.setHgap(10); // Horizontal gap between columns
            gp.setVgap(5);  // Vertical gap between rows

            // Make sure the GridPane takes the full width of the VBox
            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);
            GridPane.setHgrow(gp, Priority.SOMETIMES);

            vBoxContent.getChildren().add(gp);
        }
        //vBoxContent.layout();
    }
}
