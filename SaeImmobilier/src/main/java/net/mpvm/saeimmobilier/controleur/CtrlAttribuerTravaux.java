package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Travaux;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewTravaux;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.mpvm.saeimmobilier.util.JfxUtil.*;

public class CtrlAttribuerTravaux {

    public static final String CRÉER_TRAVAUX = "Créer Travaux";
    public static final String RETOUR_AUX_IMMEUBLES = "Retour aux immeubles";
    public static final String TRAVAUX_À_ATTRIBUER = "Travaux à Attribuer";
    public static final String ID_TRAVAUX = "ID Travaux : ";
    public static final String NUMÉRO_FACTURE = "Numéro Facture : ";
    public static final String ENTREPRISE = "Entreprise : ";
    public static final String MONTANT = "Montant : ";
    public static final String MONTANT_NON_DÉDUCTIBLE = "Montant Non Déductible : ";
    public static final String RÉDUCTION = "Réduction : ";
    public static final String DATE_TRAVAUX = "Date Travaux : ";
    public static final String NATURE = "Nature : ";
    public static final String NUMÉRO_DEVIS = "Numéro Devis : ";
    public static final String ID_BIEN = "ID Bien : ";
    public static final String SUPPRIMER_LES_TRAVAUX = "Supprimer les travaux";
    public static final String CHOISIR = "  Choisir  ";
    public static final String CONFIRMATION_DE_LA_SUPPRESSION = "Confirmation de la suppression";
    public static final String SOUHAITEZ_VOUS_RÉELLEMENT_SUPPRIMER_CES_TRAVAUX = "Souhaitez-vous réellement supprimer ces travaux ?";
    public static final String CETTE_ACTION_EST_IRRÉVERSIBLE = "Cette action est irréversible";
    @FXML
    public VBox vBoxContent;

    private Map<Integer, Travaux> travaux;
    private int idBien;

    public void initialize() {
        vBoxContent.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBien(stage);
                    try {
                        afficheTravaux();
                    } catch (Travaux.TravauxException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Pas de stage");
                }
            } else {
                System.out.println("Pas de scène");
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

    private void afficheTravaux() throws Travaux.TravauxException {
        try {
            travaux = Travaux.findAll().stream().collect(Collectors.toMap(Travaux::getIdTravaux, Function.identity()));
        } catch (Travaux.TravauxException travauxException) {
            JfxUtil.displayError("Erreur lors de la récupération des travaux", "Aucuns Travaux trouvés");
            travauxException.printStackTrace();
            return;
        }

        Label titre = new Label(TRAVAUX_À_ATTRIBUER);
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        titre.setAlignment(Pos.CENTER);

        vBoxContent.getChildren().clear();
        vBoxContent.getChildren().add(titre);
        Button retourImmeubles = new Button(RETOUR_AUX_IMMEUBLES);
        retourImmeubles.setOnAction(this::retourImmeubles);
        retourImmeubles.getStyleClass().add(BUTTON_SUPPRIMER);
        vBoxContent.getChildren().add(retourImmeubles);

        Button creerTravaux = new Button(CRÉER_TRAVAUX);
        creerTravaux.setOnAction(this::creerTravaux);
        creerTravaux.getStyleClass().add(JfxUtil.BUTTON_VALIDER);
        vBoxContent.getChildren().add(creerTravaux);

        for (Travaux t : travaux.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(150);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1, col1);

            Label idTravauxLabel = new Label(ID_TRAVAUX + t.getIdTravaux());
            Label numeroFacture = new Label(NUMÉRO_FACTURE + t.getNumeroFacture());
            Label entreprise = new Label(ENTREPRISE + t.getEntreprise());
            Label montant = new Label(MONTANT + t.getMontant());
            Label montantNonDeductible = new Label(MONTANT_NON_DÉDUCTIBLE + t.getMontantNonDeductible());
            Label reduction = new Label(RÉDUCTION + t.getReduction());
            Label dateTravaux = new Label(DATE_TRAVAUX + t.getDateTravaux());
            Label nature = new Label(NATURE + t.getNature());
            Label numeroDevis = new Label(NUMÉRO_DEVIS + t.getNumeroDevis());
            Label idBienLabel = new Label(ID_BIEN + t.setIdBienTravaux(t));

            Button deleteButton = new Button(SUPPRIMER_LES_TRAVAUX);
            Button chooseButton = new Button(CHOISIR);

            deleteButton.setOnAction(event -> JfxUtil.setAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION_DE_LA_SUPPRESSION, SOUHAITEZ_VOUS_RÉELLEMENT_SUPPRIMER_CES_TRAVAUX, CETTE_ACTION_EST_IRRÉVERSIBLE)
                    .filter(r -> r.equals(ButtonType.OK))
                    .ifPresent(r -> {
                        try {
                            deleteTravaux(t.getIdTravaux());
                        } catch (Travaux.TravauxException e) {
                            throw new RuntimeException(e);
                        }
                    }));
            chooseButton.setOnAction(event -> attribuerTravaux(this.idBien, t));

            idTravauxLabel.getStyleClass().add(ASSURANCE_TITLE);
            JfxUtil.setClass(ASSURANCE_LABEL,idTravauxLabel,numeroFacture,entreprise,montant,montantNonDeductible,reduction,dateTravaux,nature,numeroDevis,idBienLabel);
            deleteButton.getStyleClass().add(BUTTON_SUPPRIMER);
            chooseButton.getStyleClass().add(JfxUtil.BUTTON_VALIDER);

            gp.add(idTravauxLabel, 0, 0);
            gp.add(numeroFacture, 1, 0);
            gp.add(entreprise, 2, 0);
            gp.add(montant, 0, 1);
            gp.add(montantNonDeductible, 1, 1);
            gp.add(reduction, 2, 1);
            gp.add(dateTravaux, 0, 2);
            gp.add(nature, 1, 2);
            gp.add(numeroDevis, 2, 2);
            gp.add(idBienLabel, 0, 3);
            gp.add(deleteButton, 1, 4);
            gp.add(chooseButton, 2, 4);

            gp.setAlignment(Pos.TOP_CENTER);
            gp.setHgap(10);
            gp.setVgap(5);

            gp.getStyleClass().add(ASSURANCE_GRIDPANE);
            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);

            vBoxContent.getChildren().add(gp);
        }
    }

    private void creerTravaux(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        s.setOnHidden(e -> {
            try {
                afficheTravaux();
            } catch (Travaux.TravauxException ex) {
                throw new RuntimeException(ex);
            }
        });

        JfxUtil.showWindow(s, VueNewTravaux.class);
    }

    @FXML
    private void retourImmeubles(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    void attribuerTravaux(int idBien, Travaux travaux) {
        if (travaux == null) {
            JfxUtil.displayError("Erreur", "Les travaux sélectionnés sont invalides.");
            return;
        }

        try {
            travaux.attribuerDesTravaux(idBien, travaux.getIdTravaux());
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Attribution réussie", "Les travaux ont été attribués avec succès au bien !");
            afficheTravaux();

        } catch (Travaux.TravauxException travauxException) {
            travauxException.printStackTrace();
            JfxUtil.displayError(
                    "Erreur lors de l'attribution des travaux",
                    ID_BIEN + idBien + "\nID Travaux : " + travaux.getIdTravaux() +
                            "\nVérifiez que les travaux sont compatibles avec le bien sélectionné."

            );
        }
    }

    private void deleteTravaux(int id) throws Travaux.TravauxException {
        try {
            travaux.get(id).delete();
            afficheTravaux();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Suppression réussie", null, "Les travaux ont été supprimés avec succès.");
        } catch (Travaux.TravauxException e) {
            JfxUtil.displayError("Erreur lors de la suppression des travaux", e.getMessage());
        }
    }
}
