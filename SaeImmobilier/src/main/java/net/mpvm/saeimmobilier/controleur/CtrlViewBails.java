package net.mpvm.saeimmobilier.controleur;

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
import net.mpvm.saeimmobilier.vue.VueAccueil;

import java.util.List;

public class CtrlViewBails {

    @FXML
    private VBox vBoxBails;

    @FXML
    private Button retourBiens;

    private int idBien;

    @FXML
    public void initialize() {
        afficheBails();
    }

    public void setIdBien(int idBien) {
        this.idBien = idBien;
        afficheBails();
    }

    private void afficheBails() {
        try {
            Label titre = new Label("Liste des Baux");
            titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
            titre.setAlignment(Pos.CENTER);
            vBoxBails.getChildren().clear();
            vBoxBails.getChildren().add(titre);

            List<Bail> baux = Bail.findByBien(idBien);

            if (baux.isEmpty()) {
                Label label = new Label("Aucun bail trouvé.");
                label.getStyleClass().add("bail-title");
                vBoxBails.getChildren().add(label);
                return;
            }

            retourBiens = new Button("Retour aux biens");
            retourBiens.setOnAction(event -> retourBiens());
            retourBiens.getStyleClass().add("button-supprimer");
            vBoxBails.getChildren().add(retourBiens);

            for (Bail bail : baux) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(5);
                gp.setAlignment(Pos.TOP_CENTER);
                gp.getStyleClass().add("bail-gridpane");

                Label dateDebut = new Label("Début " + bail.getDateDebut());
                Label dateFin = new Label("Fin " + bail.getDateFin());
                Label montantLoyer = new Label("Loyer " + bail.getLoyer() + " €");
                Label dateSignature = new Label("Signature " + bail.getDateSignature());
                Label colocation = new Label("Colocation "+bail.getColocation());


                dateDebut.getStyleClass().add("assurance-label");
                dateFin.getStyleClass().add("assurance-label");
                montantLoyer.getStyleClass().add("assurance-label");
                dateSignature.getStyleClass().add("assurance-label");
                colocation.getStyleClass().add("assurance-label");

                gp.add(dateDebut, 0, 0);
                gp.add(dateFin, 1, 0);
                gp.add(montantLoyer, 2, 0);
                gp.add(dateSignature, 3, 0);

                vBoxBails.getChildren().add(gp);
            }
        } catch (Bail.BailException e) {
            JfxUtil.displayError("Erreur lors du chargement des baux", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void retourBiens() {
        Stage stage = (Stage) retourBiens.getScene().getWindow();
        stage.close();
    }
}
