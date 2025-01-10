package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

import java.util.List;


public class CtrlViewBiensLouables {

    @FXML
    private VBox vBoxBiensLouables;
    @FXML
    private Button retourAccueil;

    private int idImmeuble;

    @FXML
    public void initialize() {
        vBoxBiensLouables.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdImmeuble(stage);
                    afficheBiens();
                } else {
                    System.out.println("Pas de stage");
                }
            } else {
                System.out.println("Pas de scène");
            }
        });
    }

    public void setIdImmeuble(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.idImmeuble = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }



    private void afficheBiens() {
        try {
            Label titre = new Label("Liste des Biens Louables");
            titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
            titre.setAlignment(Pos.CENTER);

            vBoxBiensLouables.getChildren().clear();
            vBoxBiensLouables.getChildren().add(titre);
            Button retourImmeubles = new Button("Retour aux immeubles");
            retourImmeubles.setOnAction(this::retourImmeubles);
            retourImmeubles.getStyleClass().add("button-supprimer");
            vBoxBiensLouables.getChildren().add(retourImmeubles);
            List<BienLouable> biens = BienLouable.findByImmeuble(idImmeuble);
            Button ajouterBien = new Button("Ajouter Bien");
            ajouterBien.setOnAction(event -> {
                try {
                    ajouterBien(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            ajouterBien.getStyleClass().add("button-valider");
            vBoxBiensLouables.getChildren().add(ajouterBien);
            if (biens.isEmpty()) {
                Label label = new Label("Aucun bien trouvé.");
                label.getStyleClass().add("assurance-title");
                vBoxBiensLouables.getChildren().add(label);
                return;
            }

            for (BienLouable bien : biens) {

                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("locataire-gridpane");
                Label Nom = new Label("Nom " + bien.getIdProprio());
                Label typeBien = new Label("Type " + bien.getTypeBien());
                Label CAdresse = new Label("Complément d'Adresse " + bien.getComplementAdresse());
                Label surface = new Label("Surface " + bien.getSurface() + " m²");
                Label nbPieces = new Label("Pièces " + bien.getNbPieces());

                Button gererBailsButton = new Button("Gérer Bails");
                gererBailsButton.setOnAction(event -> gererBails(bien.getIdBien(),event));

                Button attribuerAssuranceButton = new Button("Attribuer Assurance");
                attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(bien.getIdBien(),event));

                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(event -> supprimerBien(bien));

                List<Label> labels = List.of(Nom,typeBien, CAdresse, surface, nbPieces);
                Nom.getStyleClass().add("assurance-title");
                labels.forEach(label -> label.getStyleClass().add("assurance-label"));

                gererBailsButton.getStyleClass().add("button-valider");
                attribuerAssuranceButton.getStyleClass().add("button-valider");
                supprimerButton.getStyleClass().add("button-supprimer");
                gp.add(Nom,0,0);
                gp.add(typeBien, 0, 1);
                gp.add(CAdresse, 1, 0);
                gp.add(surface, 1, 1);
                gp.add(nbPieces, 2, 1);
                gp.add(gererBailsButton, 3, 0);
                gp.add(attribuerAssuranceButton, 4, 0);
                gp.add(supprimerButton, 3, 1);

                vBoxBiensLouables.getChildren().add(gp);
            }
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors du chargement des biens", e.getMessage());
            e.printStackTrace();
        }
    }

    private void supprimerBien(BienLouable bien) {
        try {
            bien.delete(); // Suppression de l'objet Bien
            afficheBiens(); // Mise à jour de l'affichage des biens
            JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                    "Suppression réussie",
                    null,
                    "Le bien a été supprimé avec succès."); // Affichage d'une alerte d'information
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors de la suppression", e.getMessage()); // Gestion des erreurs avec l'alerte
        }
    }

    private void gererBails(int idBien,ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bien", idBien);
        new VueBails().startForBiensLouables(s, idBien);
    }

    private void attribuerAssurance(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        new VueAttribuerAssurance(idBien).startForImmeuble(s);
    }
    private void ajouterBien(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        new VueNewBien().start(s);
    }
    @FXML
    private void retourImmeubles(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        new VueImmeubles().start(new Stage());
    }
}
