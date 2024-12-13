package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.sql.SQLException;
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

    private void afficheBiens() {
        List<Bien> biens = null; // Méthode pour filtrer les biens
        try {
            biens = Bien.findByImmeuble(idImmeuble);
        } catch (Bien.BienException e) {
            e.printStackTrace();
            e.getSqlException().printStackTrace();
        }
        gridPaneBiensLouables.getChildren().clear();

        for (Bien bien : biens) {
            Label label = new Label(bien.getAdresse());
            gridPaneBiensLouables.add(label, 0, gridPaneBiensLouables.getChildren().size());
        }
    }
}
