package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.util.List;

public class CtrlViewBiensLouables {

    @FXML
    private VBox vBoxBiensLouables;

    @FXML
    private GridPane gridPaneBiensLouables;

    private int idImmeuble;

    public void setIdImmeuble(int idImmeuble) throws Queryable.QueryableException {
        this.idImmeuble = idImmeuble;
        afficheBiens();
    }

    private void afficheBiens() throws Queryable.QueryableException {
        List<Bien> biens = Bien.findByImmeuble(idImmeuble); // Méthode pour filtrer les biens
        gridPaneBiensLouables.getChildren().clear();

        for (Bien bien : biens) {
            Label label = new Label(bien.getAdresse());
            gridPaneBiensLouables.add(label, 0, gridPaneBiensLouables.getChildren().size());
        }
    }
}
