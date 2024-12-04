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
import net.mpvm.saeimmobilier.modele.Locataire;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlViewLocataires {

    @FXML
    public VBox vBoxContent;

    private Map<Integer, Locataire> locataires;

    public void initialize(){
        afficheLocataires();
    }

    private void afficheLocataires() {
        try {
            locataires = Locataire.findALl().stream().collect(Collectors.toMap(Locataire::getIdLocataire, Function.identity()));
        } catch (Locataire.LocataireException e) {
            locataires = new HashMap<>();
        }
        vBoxContent.getChildren().clear();
        for(Locataire l : locataires.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(100);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1);

            Label nom = new Label("Nom : " + l.getNom());
            Label prenom = new Label("Prenom : " + l.getPrenom());
            Label email = new Label("Email : " + l.getEmail());
            Label telephone = new Label("N° tel. : " + l.getTelephone());
            Label sexe = new Label("Sexe : " + l.getSexe());
            Button button = new Button("Supprimer le locataire");
            button.setOnAction(event -> askForDelete(l.getIdLocataire()));

            nom.getStyleClass().add("locataire-label");
            prenom.getStyleClass().add("locataire-label");
            email.getStyleClass().add("locataire-label");
            telephone.getStyleClass().add("locataire-label");
            sexe.getStyleClass().add("locataire-label");
            button.getStyleClass().add("locataire-button");
            gp.add(nom, 0, 0);
            gp.add(prenom, 0, 1);
            gp.add(email, 1, 1);
            gp.add(telephone, 1, 0);
            gp.add(sexe, 2, 2);

            button.setId(String.valueOf(l.getIdLocataire()));
            gp.add(button, 2, 1);

            gp.setAlignment(Pos.TOP_CENTER);

            GridPane.setHalignment(nom, HPos.LEFT);
            GridPane.setHalignment(prenom, HPos.LEFT);
            GridPane.setHalignment(email, HPos.LEFT);
            GridPane.setHalignment(telephone, HPos.LEFT);
            GridPane.setHalignment(sexe, HPos.LEFT);
            GridPane.setValignment(nom, VPos.CENTER);
            GridPane.setValignment(prenom, VPos.CENTER);
            GridPane.setValignment(email, VPos.CENTER);
            GridPane.setValignment(telephone, VPos.CENTER);
            GridPane.setValignment(sexe, VPos.CENTER);

            gp.getStyleClass().add("locataire-gridpane");
            gp.setHgap(10);
            gp.setVgap(5);

            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);

            vBoxContent.getChildren().add(gp);
        }
    }

    @FXML
    public void askForDelete(int id){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de la suppression");
        alert.setHeaderText("Souhaitez-vous réellement supprimer ce locataire? ");
        alert.setContentText("Cette action est irréversible");
        alert.showAndWait()
                .filter(r -> r.equals(ButtonType.OK))
                .ifPresent(r -> deleteLocataire(id));
    }

    private void deleteLocataire(int id){
        try {
            locataires.get(id).delete();
        } catch (Locataire.LocataireException e) {
            // TODO: handle exception with visual
        }
        afficheLocataires();
    }
}
