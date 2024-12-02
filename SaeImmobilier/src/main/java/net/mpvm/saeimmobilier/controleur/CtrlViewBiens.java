package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlViewBiens {

    @FXML
    public VBox vBoxContent;

    private Map<Integer, Bien> biens;

    public void initialize() {
        afficheBiens();
    }

    private void afficheBiens() {
        try {
            biens = Bien.findAll().stream().collect(Collectors.toMap(Bien::getIdBien, Function.identity()));
        } catch (Bien.BienException e) {
            biens = new HashMap<>();
        }
        vBoxContent.getChildren().clear();

        for (Bien b : biens.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(100);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1, col1);

            Label adresse = new Label("Adresse : " + b.getAdresse());
            Label ville = new Label("Ville : " + b.getVille());
            Label codePostal = new Label("Code Postal : " + b.getCodePostal());
            Label typeBien = new Label("Type : " + b.getTypeBien());
            Label surface = new Label("Surface : " + b.getSurface() + " m²");
            Label nombrePieces = new Label("Pièces : " + b.getNombrePieces());
            Button button = new Button("Supprimer le bien");
            button.setOnAction(event -> askForDelete(b.getIdBien()));

            gp.add(adresse, 0, 0);
            gp.add(ville, 1, 0);
            gp.add(codePostal, 2, 0);
            gp.add(typeBien, 0, 1);
            gp.add(surface, 1, 1);
            gp.add(nombrePieces, 2, 1);
            gp.add(button, 1, 2);

            gp.setAlignment(Pos.TOP_CENTER);

            GridPane.setHalignment(adresse, HPos.LEFT);
            GridPane.setHalignment(ville, HPos.LEFT);
            GridPane.setHalignment(codePostal, HPos.LEFT);
            GridPane.setHalignment(typeBien, HPos.LEFT);
            GridPane.setHalignment(surface, HPos.LEFT);
            GridPane.setHalignment(nombrePieces, HPos.LEFT);

            gp.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");
            gp.setHgap(10); // Horizontal gap between columns
            gp.setVgap(5);  // Vertical gap between rows

            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);
            GridPane.setHgrow(gp, Priority.SOMETIMES);

            vBoxContent.getChildren().add(gp);
        }
    }

    @FXML
    public void askForDelete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de la suppression");
        alert.setHeaderText("Souhaitez-vous réellement supprimer ce bien ?");
        alert.setContentText("Cette action est irréversible.");
        alert.showAndWait()
                .filter(r -> r.equals(ButtonType.OK))
                .ifPresent(r -> deleteBien(id));
    }

    private void deleteBien(int id) {
        try {
            biens.get(id).delete();
        } catch (Bien.QueryableException e) {
            throw new RuntimeException(e);
        }
        afficheBiens();
    }
}
