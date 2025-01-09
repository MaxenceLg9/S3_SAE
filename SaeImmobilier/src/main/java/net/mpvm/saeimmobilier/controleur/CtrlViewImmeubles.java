package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.VueAttribuerAssurance;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.List;

public class CtrlViewImmeubles {

    @FXML
    private VBox vBoxImmeubles;

    @FXML
    private Button retourAccueil;

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
            retourAccueil.setOnAction(event -> retourAccueil());
            retourAccueil.getStyleClass().add("button-supprimer");
            vBoxImmeubles.getChildren().add(retourAccueil);

            List<Immeuble> immeubles = Immeuble.findAll();

            if (immeubles.isEmpty()) {
                Label label = new Label("Aucun immeuble trouvé.");
                label.getStyleClass().add("assurance-label");
                vBoxImmeubles.getChildren().add(label);
                return;
            }

            // Ajouter les immeubles à la VBox
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
                Label nbAppartements = new Label("Nombre d'appartements " + immeuble.getNbAppartements(immeuble.getIdBien()));

                Button voirBiensButton = new Button("Voir les biens");
                voirBiensButton.setOnAction(event -> afficheBiensPourImmeuble(immeuble.getIdBien()));

                Button attribuerAssuranceButton = new Button("Attribuer Assurance");
                attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(immeuble.getIdBien()));

                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(event -> supprimerImmeuble(immeuble));
                nom.getStyleClass().add("assurance-label");
                nom.getStyleClass().add("assurance-title");
                adresse.getStyleClass().add("assurance-label");

                codepostal.getStyleClass().add("assurance-label");
                ville.getStyleClass().add("assurance-label");
                nbAppartements.getStyleClass().add("assurance-label");
                voirBiensButton.getStyleClass().add("button-valider");
                attribuerAssuranceButton.getStyleClass().add("button-valider");
                supprimerButton.getStyleClass().add("button-supprimer");
                gp.add(nom, 0, 0);
                gp.add(adresse, 1, 0);
                gp.add(codepostal, 2, 0);
                gp.add(ville, 3, 0);
                gp.add(nbAppartements, 4, 0);
                gp.add(voirBiensButton, 5, 0);
                gp.add(attribuerAssuranceButton, 6, 0);
                gp.add(supprimerButton, 7, 0);

                vBoxImmeubles.getChildren().add(gp);
            }
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.displayError("Erreur lors du chargement des immeubles", e.getMessage());
            e.printStackTrace();
        }
    }


    private void supprimerImmeuble(Immeuble immeuble) {
        try {
            // Suppression de l'immeuble
            immeuble.delete();

            // Rafraîchir l'affichage
            afficheImmeubles();

            // Afficher une alerte de confirmation
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

    private void afficheBiensPourImmeuble(int idBien) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        new VueBiensLouables(idBien).startForImmeuble(s);
    }

    private void attribuerAssurance(int idBien) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueAttribuerAssurance.class);
    }

    @FXML
    private void retourAccueil() {
        new VueAccueil().start(new Stage());
    }
}
