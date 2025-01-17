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
import net.mpvm.saeimmobilier.modele.Bail;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

import java.util.List;

public class CtrlViewBails {

    @FXML
    private VBox vBoxBails;

    @FXML
    private Button retourBiens;

    private int idBien;

    @FXML
    public void initialize() {
        vBoxBails.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBien(stage);
                    afficheBails();
                } else {
                    System.out.println("pas de stage");
                }
            } else {
                System.out.println("pas de scène");
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

    private void afficheBails() {
        try {
            Label titre = new Label("Liste des Bails");
            titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
            titre.setAlignment(Pos.CENTER);

            vBoxBails.getChildren().clear();
            vBoxBails.getChildren().add(titre);

            retourBiens = new Button("Retour aux biens");
            retourBiens.setOnAction(event -> retourBiens(event));
            retourBiens.getStyleClass().add("button-supprimer");
            vBoxBails.getChildren().add(retourBiens);
            Button ajouterBail = new Button("Ajouter Bail");
            ajouterBail.setOnAction(event -> ajouterBail(event,idBien));
            ajouterBail.getStyleClass().add("button-valider");
            vBoxBails.getChildren().add(ajouterBail);
            List<Bail> baux = Bail.findByBien(idBien);

            if (baux.isEmpty()) {
                Label label = new Label("Aucun bail trouvé.");
                label.getStyleClass().add("assurance-title");
                vBoxBails.getChildren().add(label);
                return;
            }

            for (Bail bail : baux) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("locataire-gridpane");

                Label dateDebut = new Label("Début " + bail.getDateDebut());
                Label dateFin = new Label("Fin " + bail.getDateFin());
                Label montantLoyer = new Label("Loyer " + bail.getLoyer() + " €");
                Label dateSignature = new Label("Signature " + bail.getDateSignature());
                Label colocation = new Label("Colocation " + bail.isColocation());

                Button gererLocatairesButton = new Button("Gérer Locataires");
                gererLocatairesButton.setOnAction(event -> gererLocataires(bail.getIdBail(),event));

                Button resilierBailButton = new Button("Résilier");
                resilierBailButton.setOnAction(event -> resilierBail(bail));
                Button creerCharges = new Button("Attribuer Charges");
                creerCharges.setOnAction(event -> creerCharges(bail.getIdBail(),event));
                Button voirDocument = new Button("Voir Document");
                voirDocument.setOnAction(event -> voirDocument(bail.getIdBail(),event));
                Button revaloriserLoyer = new Button("Revaloriser Loyer");
                revaloriserLoyer.setOnAction(event -> bail.revaloriserLoyer(1));
                // Application des styles
                List<Label> labels = List.of(dateDebut, dateFin, montantLoyer, dateSignature, colocation);
                labels.forEach(label -> label.getStyleClass().add("assurance-label"));
                dateDebut.getStyleClass().add("assurance-title");
                dateFin.getStyleClass().add("assurance-title");
                voirDocument.getStyleClass().add("button-valider");
                creerCharges.getStyleClass().add("button-valider");
                revaloriserLoyer.getStyleClass().add("button-valider");
                gererLocatairesButton.getStyleClass().add("button-valider");
                resilierBailButton.getStyleClass().add("button-supprimer");

                gp.add(dateDebut, 0, 0);
                gp.add(dateFin, 1, 0);
                gp.add(montantLoyer, 0, 1);
                gp.add(dateSignature, 1, 1);
                gp.add(colocation, 2, 1);
                gp.add(gererLocatairesButton, 5, 0);
                gp.add(resilierBailButton, 7, 0);
                gp.add(creerCharges, 6, 0);
                gp.add(revaloriserLoyer, 6, 1);
                gp.add(voirDocument, 5, 1);


                vBoxBails.getChildren().add(gp);
            }
        } catch (Bail.BailException e) {
            JfxUtil.displayError("Erreur lors du chargement des baux", e.getMessage());
            e.printStackTrace();
        }
    }

    private void gererLocataires(int idBail, ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bail",idBail);
        JfxUtil.showWindow(s,VueLocataires.class);

    }
    private void ajouterBail(ActionEvent event, int idBien) {
        Stage s = new Stage();
        s.getProperties().put("idBien",idBien);
        JfxUtil.showWindow(s, VueCreerUnBail.class);
    }
    private void resilierBail(Bail bail) {
        try {
            bail.delete();
            afficheBails();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                    "Résiliation réussie",
                    null,
                    "Le bail a été résilié avec succès.");
        } catch (Bail.BailException e) {
            JfxUtil.displayError("Erreur lors de la résiliation", e.getMessage());
        }
    }
    private void creerCharges(int idBail,ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bail",idBail);
        JfxUtil.showWindow(s,VueCharges.class);
    }
    private void voirDocument(int idBail,ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bail",idBail);
        JfxUtil.showWindow(s,VueCharges.class);
    }

    @FXML
    private void retourBiens(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueBiensLouables.class);
    }
}
