package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;
import net.mpvm.saeimmobilier.vue.VueHome;
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
            List<Immeuble> immeubles = Immeuble.findAll();
            vBoxImmeubles.getChildren().clear();

            if (immeubles.isEmpty()) {
                Label label = new Label("Aucun immeuble trouvé.");
                label.setStyle("-fx-text-fill: white;");
                vBoxImmeubles.getChildren().add(label);
                return;
            }

            // Créer l'en-tête de la grille
            GridPane header = new GridPane();

            header.setAlignment(Pos.TOP_CENTER);
            header.setStyle("-fx-border-color: white; -fx-border-radius: 10;");

            Label titre = new Label("Vue Immeubles");
            Button retour =new Button("Retour");

            titre.getStyleClass().add("assurance-title");


            header.add(titre, 0, 0);


            vBoxImmeubles.getChildren().add(header);

            for (Immeuble immeuble : immeubles) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("locataire-gridpane");

                Label adresse = new Label("Adresse : " + immeuble.getAdresse());
                Label codepostal= new Label("Code Postal : " + immeuble.getCodePostal());
                Label ville = new Label("Ville : " + immeuble.getVille());
                Label nbAppartements = new Label("Nombre d'appartements : " + immeuble.getNbAppartements());
                Button button = new Button("Voir les biens");
                button.setOnAction(event -> afficheBiensPourImmeuble(immeuble.getIdBien()));

                codepostal.getStyleClass().add("assurance-label");
                ville.getStyleClass().add("assurance-label");
                adresse.getStyleClass().add("assurance-label");
                adresse.getStyleClass().add("assurance-title");
                nbAppartements.getStyleClass().add("assurance-label");
                button.getStyleClass().add("button-valider");

                gp.add(adresse, 0, 1);
                gp.add(codepostal, 1, 1);
                gp.add(ville, 2, 1);
                gp.add(nbAppartements, 3, 1);
                gp.add(button, 3, 2);

                vBoxImmeubles.getChildren().add(gp);
            }
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.displayError("Erreur lors du chargement des immeubles", e.getMessage());
            e.printStackTrace();
        }
    }
    private void afficheBiensPourImmeuble(int idImmeuble) {
        new VueBiensLouables().startForImmeuble(new Stage(), idImmeuble);
    }
    @FXML
    private void retourAccueil() {
        new VueHome().start(new Stage());
    }
}
