package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Travaux;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueImmeubles;
import net.mpvm.saeimmobilier.vue.VueNewTravaux;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlAttribuerTravaux {

    @FXML
    public VBox vBoxContent;

    private Map<Integer, Travaux> travaux;
    private int idBien;

    public void initialize() {
        vBoxContent.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBien(stage);
                    try {
                        afficheTravaux();
                    } catch (Travaux.TravauxException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Pas de stage");
                }
            } else {
                System.out.println("Pas de scène");
            }
        });
    }

    public void setIdBien(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.idBien = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }

    private void afficheTravaux() throws Travaux.TravauxException {
        try {
            travaux = Travaux.findAll().stream().collect(Collectors.toMap(Travaux::getIdTravaux, Function.identity()));
        } catch (Travaux.TravauxException travauxException) {
            JfxUtil.displayError("Erreur lors de la récupération des travaux", "Aucuns Travaux trouvés");
            travauxException.printStackTrace();
            return;
        }

        Label titre = new Label("Travaux à Attribuer");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        titre.setAlignment(Pos.CENTER);

        vBoxContent.getChildren().clear();
        vBoxContent.getChildren().add(titre);
        Button retourImmeubles = new Button("Retour aux immeubles");
        retourImmeubles.setOnAction(event -> retourImmeubles(event));
        retourImmeubles.getStyleClass().add("button-supprimer");
        vBoxContent.getChildren().add(retourImmeubles);

        Button creerTravaux = new Button("Créer Travaux");
        creerTravaux.setOnAction(event -> creerTravaux(event));
        creerTravaux.getStyleClass().add("button-valider");
        vBoxContent.getChildren().add(creerTravaux);

        for (Travaux t : travaux.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(150);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1, col1);

            Label idTravauxLabel = new Label("ID Travaux : " + t.getIdTravaux());
            Label numeroFacture = new Label("Numéro Facture : " + t.getNumeroFacture());
            Label entreprise = new Label("Entreprise : " + t.getEntreprise());
            Label montant = new Label("Montant : " + t.getMontant());
            Label montantNonDeductible = new Label("Montant Non Déductible : " + t.getMontantNonDeductible());
            Label reduction = new Label("Réduction : " + t.getReduction());
            Label dateTravaux = new Label("Date Travaux : " + t.getDateTravaux());
            Label nature = new Label("Nature : " + t.getNature());
            Label numeroDevis = new Label("Numéro Devis : " + t.getNumeroDevis());
            Label idBienLabel = new Label("ID Bien : " + t.setIdBienTravaux(t));

            Button deleteButton = new Button("Supprimer les travaux");
            Button chooseButton = new Button("  Choisir  ");

            deleteButton.setOnAction(event -> askForDelete(t.getIdTravaux()));
            chooseButton.setOnAction(event -> attribuerTravaux(this.idBien, t));

            idTravauxLabel.getStyleClass().add("assurance-label");
            idTravauxLabel.getStyleClass().add("assurance-title");
            numeroFacture.getStyleClass().add("assurance-label");
            entreprise.getStyleClass().add("assurance-label");
            montant.getStyleClass().add("assurance-label");
            montantNonDeductible.getStyleClass().add("assurance-label");
            reduction.getStyleClass().add("assurance-label");
            dateTravaux.getStyleClass().add("assurance-label");
            nature.getStyleClass().add("assurance-label");
            numeroDevis.getStyleClass().add("assurance-label");
            idBienLabel.getStyleClass().add("assurance-label");
            deleteButton.getStyleClass().add("button-supprimer");
            chooseButton.getStyleClass().add("button-valider");

            gp.add(idTravauxLabel, 0, 0);
            gp.add(numeroFacture, 1, 0);
            gp.add(entreprise, 2, 0);
            gp.add(montant, 0, 1);
            gp.add(montantNonDeductible, 1, 1);
            gp.add(reduction, 2, 1);
            gp.add(dateTravaux, 0, 2);
            gp.add(nature, 1, 2);
            gp.add(numeroDevis, 2, 2);
            gp.add(idBienLabel, 0, 3);
            gp.add(deleteButton, 1, 4);
            gp.add(chooseButton, 2, 4);

            gp.setAlignment(Pos.TOP_CENTER);
            gp.setHgap(10);
            gp.setVgap(5);

            gp.getStyleClass().add("assurance-gridpane");
            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);

            vBoxContent.getChildren().add(gp);
        }
    }

    private void creerTravaux(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        JfxUtil.showWindow(s, VueNewTravaux.class);
    }

    @FXML
    private void retourImmeubles(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Stage s=new Stage();
        JfxUtil.showWindow(s, VueImmeubles.class);
    }

    public void attribuerTravaux(int idBien, Travaux travaux) {
        if (travaux == null) {
            JfxUtil.displayError("Erreur", "Les travaux sélectionnés sont invalides.");
            return;
        }

        try {
            travaux.attribuerDesTravaux(idBien, travaux.getIdTravaux());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Attribution réussie");
            alert.setContentText("Les travaux ont été attribués avec succès au bien !");
            alert.showAndWait();

            afficheTravaux();

        } catch (Travaux.TravauxException travauxException) {
            travauxException.printStackTrace();
            JfxUtil.displayError(
                    "Erreur lors de l'attribution des travaux",
                    "ID Bien : " + idBien + "\nID Travaux : " + travaux.getIdTravaux() +
                            "\nVérifiez que les travaux sont compatibles avec le bien sélectionné."

            );
        }
    }

    @FXML
    public void askForDelete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de la suppression");
        alert.setHeaderText("Souhaitez-vous réellement supprimer ces travaux ?");
        alert.setContentText("Cette action est irréversible");
        alert.showAndWait()
                .filter(r -> r.equals(ButtonType.OK))
                .ifPresent(r -> {
                    try {
                        deleteTravaux(id);
                    } catch (Travaux.TravauxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void deleteTravaux(int id) throws Travaux.TravauxException {
        try {
            travaux.get(id).delete();
            afficheTravaux();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les travaux ont été supprimés avec succès.");
            alert.showAndWait();
        } catch (Travaux.TravauxException e) {
            JfxUtil.displayError("Erreur lors de la suppression des travaux", e.getMessage());
        }
    }
}
