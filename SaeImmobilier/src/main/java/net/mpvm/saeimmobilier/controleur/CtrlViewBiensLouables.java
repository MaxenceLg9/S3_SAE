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
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.vue.VueAttribuerAssurance;
import net.mpvm.saeimmobilier.vue.VueBails;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;

import java.util.List;

public class CtrlViewBiensLouables {

    @FXML
    private VBox vBoxBiensLouables;

    @FXML
    private Button retourAccueil;

    private int idImmeuble;

    @FXML
    public void initialize() {
        afficheBiens();
    }

    public void setIdImmeuble(int idImmeuble) {
        this.idImmeuble = idImmeuble;
        afficheBiens();
    }

    private void afficheBiens() {
        try {
            Label titre = new Label("Liste des Biens Louables");
            titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
            titre.setAlignment(Pos.CENTER);
            vBoxBiensLouables.getChildren().clear();
            vBoxBiensLouables.getChildren().add(titre);

            List<Bien> biens = Bien.findByImmeuble(idImmeuble);

            if (biens.isEmpty()) {
                Label label = new Label("Aucun bien trouvé.");
                label.getStyleClass().add("assurance-title");
                vBoxBiensLouables.getChildren().add(label);
                return;
            }
            retourAccueil = new Button("Retour à l'accueil");
            retourAccueil.setOnAction(event -> retourAccueil());
            retourAccueil.getStyleClass().add("button-supprimer");
            vBoxBiensLouables.getChildren().add(retourAccueil);
            for (Bien bien : biens) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("locataire-gridpane");
                Label typeBien = new Label("Type " + bien.getTypeBien());
                Label adresse = new Label("Adresse " + bien.getAdresse());

                Label surface = new Label("Surface " + bien.getSurface() + " m²");
                Label nbPieces = new Label("Pièces " + bien.getNbPieces());

                Button gererLocatairesButton = new Button("Gérer Locataires");
                gererLocatairesButton.setOnAction(event -> gererBails(bien.getIdBien()));

                Button attribuerAssuranceButton = new Button("Attribuer Assurance");
                attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(bien.getIdBien()));

                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(event -> supprimerBien(bien));

                typeBien.getStyleClass().add("assurance-title");
                typeBien.getStyleClass().add("assurance-label");
                adresse.getStyleClass().add("assurance-label");
                surface.getStyleClass().add("assurance-label");
                nbPieces.getStyleClass().add("assurance-label");
                gererLocatairesButton.getStyleClass().add("button-valider");
                attribuerAssuranceButton.getStyleClass().add("button-valider");
                supprimerButton.getStyleClass().add("button-supprimer");

                gp.add(adresse, 1, 0);
                gp.add(typeBien, 0, 0);
                gp.add(surface, 2, 0);
                gp.add(nbPieces, 3, 0);
                gp.add(gererLocatairesButton, 4, 0);
                gp.add(attribuerAssuranceButton, 5, 0);
                gp.add(supprimerButton, 6, 0);

                vBoxBiensLouables.getChildren().add(gp);
            }
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors du chargement des biens", e.getMessage());
            e.printStackTrace();
        }
    }

    private void supprimerBien(Bien bien) {
        try {
            bien.delete();
            afficheBiens();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression réussie");
            alert.setHeaderText(null);
            alert.setContentText("Le bien a été supprimé avec succès.");
            alert.showAndWait();
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors de la suppression du bien", e.getMessage());
            e.printStackTrace();
        }
    }

    private void gererBails(int idBien) {
        new VueBails().startForBiensLouables(new Stage(), idBien);
    }

    private void attribuerAssurance(int idBien) {
        new VueAttribuerAssurance().startforBien(new Stage(), idBien);
    }

    @FXML
    private void retourAccueil() {
        new VueAccueil().start(new javafx.stage.Stage());
    }

}