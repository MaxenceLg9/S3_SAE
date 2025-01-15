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
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.*;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.List;

public class CtrlViewImmeubles {

    @FXML
    private VBox vBoxImmeubles;

    @FXML
    public void initialize() {
        afficheImmeubles();
    }



    private void afficheImmeubles() {
        try {
            // Effacer les éléments existants avant de les ajouter à nouveau
            vBoxImmeubles.getChildren().clear();

            // Ajouter le titre
            Label titre = new Label("Liste des Immeubles");
            titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
            titre.setAlignment(Pos.CENTER);
            vBoxImmeubles.getChildren().add(titre);

            // Ajouter le bouton retour à l'accueil
            Button retourAccueil = new Button("Retour à l'accueil");
            retourAccueil.setOnAction(event -> retourAccueil(event));
            retourAccueil.getStyleClass().add("button-supprimer");
            vBoxImmeubles.getChildren().add(retourAccueil);
            Button ajouterBien = new Button("Ajouter Bien");
            ajouterBien.setOnAction(event -> {
                try {
                    ajouterBien(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            ajouterBien.getStyleClass().add("button-valider");
            vBoxImmeubles.getChildren().add(ajouterBien);
            List<Immeuble> immeubles = Immeuble.findAll();

            if (immeubles.isEmpty()) {
                Label label = new Label("Aucun immeuble trouvé.");
                label.getStyleClass().add("assurance-label");
                vBoxImmeubles.getChildren().add(label);
                return;
            }

            for (Immeuble immeuble : immeubles) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("locataire-gridpane");
                Label nom = new Label("Nom " + immeuble.getIdProprio());
                Label adresse = new Label("Adresse " + immeuble.getAdresse());
                Label codepostal = new Label("Code Postal " + immeuble.getCodePostal());
                Label ville = new Label("Ville " + immeuble.getVille());
                Label nbAppartements = new Label("Nombre d'appartements " + immeuble.getNbAppartements());

                Button voirBiensButton = new Button("Voir les biens");
                voirBiensButton.setOnAction(event -> afficheBiensPourImmeuble(immeuble.getIdBien(),event));

                Button attribuerAssuranceButton = new Button("Attribuer Assurance");
                attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(immeuble.getIdBien(),event));
                Button faireTravaux = new Button("Attribuer Travaux");
                faireTravaux.setOnAction(event -> attribuerTravaux(immeuble.getIdBien(),event));
                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(event -> {if(JfxUtil.askForDelete("Voulez vous supprimer l'immeuble") == 1)
                    supprimerImmeuble(immeuble);
                });
                nom.getStyleClass().add("assurance-label");
                nom.getStyleClass().add("assurance-title");
                adresse.getStyleClass().add("assurance-label");

                codepostal.getStyleClass().add("assurance-label");
                ville.getStyleClass().add("assurance-label");
                nbAppartements.getStyleClass().add("assurance-label");
                voirBiensButton.getStyleClass().add("button-valider");
                attribuerAssuranceButton.getStyleClass().add("button-valider");
                faireTravaux.getStyleClass().add("button-valider");
                supprimerButton.getStyleClass().add("button-supprimer");
                gp.add(nom, 0, 0);
                gp.add(adresse, 0, 1);
                gp.add(codepostal, 1, 1);
                gp.add(ville, 2, 1);
                gp.add(nbAppartements, 1, 0);
                gp.add(voirBiensButton, 3, 0);
                gp.add(attribuerAssuranceButton, 4, 0);
                gp.add(faireTravaux, 3, 1);
                gp.add(supprimerButton, 4, 1);

                vBoxImmeubles.getChildren().add(gp);
            }
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.displayError("Erreur lors du chargement des immeubles", e.getMessage());
            e.printStackTrace();
        }
    }


    private void supprimerImmeuble(Immeuble immeuble) {
        try {
            immeuble.delete();
            afficheImmeubles();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression réussie");
            alert.setHeaderText(null);
            alert.setContentText("L'immeuble a été supprimé avec succès.");
            alert.showAndWait();
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.displayError("Erreur lors de la suppression de l'immeuble", e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficheBiensPourImmeuble(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueBiensLouables.class);
    }


    private void attribuerAssurance(int idBien,ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueAttribuerAssurance.class);
    }
    private void attribuerTravaux(int idBien, ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueAttribuerTravaux.class);
    }
    @FXML
    private void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private void ajouterBien(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Stage s = new Stage();
        JfxUtil.showWindow(s, VueNewBien.class);
    }
}
