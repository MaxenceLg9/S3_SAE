package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;

import java.util.List;

public class CtrlViewImmeubles {

    @FXML
    private VBox vBoxImmeubles;

    @FXML
    private GridPane gridPaneImmeubles;

    public void initialize() throws Queryable.QueryableException {
        // Chargement initial des immeubles
        afficheImmeubles();
    }

    private void afficheImmeubles() throws Queryable.QueryableException {
        List<Immeuble> immeubles = Immeuble.findAll();
        gridPaneImmeubles.getChildren().clear();

        for (Immeuble immeuble : immeubles) {
            Label label = new Label(immeuble.getAdresse());
            label.setOnMouseClicked(event -> afficheBiensPourImmeuble(immeuble.getIdBien()));
            gridPaneImmeubles.add(label, 0, gridPaneImmeubles.getChildren().size());
        }
    }


    private void afficheBiensPourImmeuble(int idImmeuble) {
        // Transition vers la fenêtre "Biens Louables" pour cet immeuble
        new VueBiensLouables().startForImmeuble(new Stage(),idImmeuble);
    }
}
