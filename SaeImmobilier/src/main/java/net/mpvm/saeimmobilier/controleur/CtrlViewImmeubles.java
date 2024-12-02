package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;

import java.util.List;

public class CtrlViewImmeubles {

    @FXML
    private VBox vBoxImmeubles;

    @FXML
    private GridPane gridPaneImmeubles;

    public void initialize() {
        // Chargement initial des immeubles
        afficheImmeubles();
    }

    private void afficheImmeubles() {
        List<Immeuble> immeubles = Bien.findAllImmeubles(); // Une méthode spécifique pour les immeubles
        gridPaneImmeubles.getChildren().clear();

        for (Bien immeuble : immeubles) {
            Label label = new Label(immeuble.getAdresse());
            label.setOnMouseClicked(event -> afficheBiensPourImmeuble(immeuble.getIdBien()));
            gridPaneImmeubles.add(label, 0, gridPaneImmeubles.getChildren().size());
        }
    }

    private void afficheBiensPourImmeuble(int idImmeuble) {
        // Transition vers la fenêtre "Biens Louables" pour cet immeuble
        new VueBiensLouables().startForImmeuble(idImmeuble);
    }
}
