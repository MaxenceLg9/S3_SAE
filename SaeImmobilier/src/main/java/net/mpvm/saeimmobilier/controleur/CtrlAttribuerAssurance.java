package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewAssurance;


import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlAttribuerAssurance {

    public static final String BUTTON_SUPPRIMER = "button-supprimer";
    public static final String BUTTON_VALIDER = "button-valider";
    private static final String ASSURANCE_LABEL_CLASS = "assurance-label";
    @FXML
    public VBox vBoxContent;

    private Map<Integer, Assurance> assurances;
    private int IdBien;


    public void initialize() {
        vBoxContent.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBien(stage);
                    try {
                        afficheAssurances();
                    } catch (Assurance.AssuranceException e) {
                        throw new IllegalArgumentException("Impossible d'afficher les assurances");
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
            this.IdBien = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }
    private void afficheAssurances() throws Assurance.AssuranceException {
        try {
            assurances = Assurance.findAll().stream().collect(Collectors.toMap(Assurance::getIdAssurance, Function.identity()));
        } catch (Assurance.AssuranceException assuranceException) {
            JfxUtil.displayError("Erreur lors de la récupération des assurances", "Vérifiez votre connexion Internet");
            return;
        }

        Label titre = new Label("Assurances à Attribuer");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        titre.setAlignment(Pos.CENTER);

        vBoxContent.getChildren().clear();
        vBoxContent.getChildren().add(titre);
        Button retourImmeubles = new Button("Retour aux immeubles");
        retourImmeubles.setOnAction(this::retourImmeubles);
        retourImmeubles.getStyleClass().add(BUTTON_SUPPRIMER);
        vBoxContent.getChildren().add(retourImmeubles);

        Button creerAssurance = new Button("Créer Assurance");
        creerAssurance.setOnAction(this::creerAssurance);
        creerAssurance.getStyleClass().add(BUTTON_VALIDER);
        vBoxContent.getChildren().add(creerAssurance);

        for (Assurance a : assurances.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(150);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1, col1);


            // Ajout des labels requis
            Label nomAssurance = new Label("Nom Assurance " + a.getNomAssurance());
            Label protectionJuridique = new Label("Protection Juridique " + a.getProtectionJuridique());
            Label prime = new Label("Prime : " + a.getPrime());
            Label typeContrat = new Label("Type de Contrat " + a.getTypeContrat());
            Label annee = new Label("Année " + a.getAnnee());
            Label totalPrime = new Label("Total Prime " + a.getTotalPrime());
            Label idBienLabel = new Label("ID Bien associé : " + a.selectIdBien());
            Button deleteButton = new Button("Supprimer l'assurance");
            Button chooseButton = new Button("  Choisir  ");

            deleteButton.setOnAction(event -> {
                if(JfxUtil.askForDelete("Voulez-vous vraiment supprimer cette assurance ?") == 1) {
                    try {
                        deleteAssurance(a.getIdAssurance());
                    } catch (Assurance.AssuranceException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            chooseButton.setOnAction(event -> attribuerAssurance(Bien.BBuilder.get(IdBien), a));

            nomAssurance.getStyleClass().add("assurance-title");
            protectionJuridique.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            prime.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            typeContrat.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            annee.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            totalPrime.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            idBienLabel.getStyleClass().add(ASSURANCE_LABEL_CLASS);
            deleteButton.getStyleClass().add(BUTTON_SUPPRIMER);
            chooseButton.getStyleClass().add(BUTTON_VALIDER);

            gp.add(nomAssurance, 0, 0);
            gp.add(annee, 0, 1);
            gp.add(prime, 1, 0);
            gp.add(typeContrat, 0, 2);
            gp.add(totalPrime, 1, 2);
            gp.add(protectionJuridique, 1, 1);
            gp.add(idBienLabel, 2, 0);
            gp.add(deleteButton, 2, 3);
            gp.add(chooseButton, 2, 1);

            gp.setAlignment(Pos.TOP_CENTER);
            gp.setHgap(10);
            gp.setVgap(5);

            gp.getStyleClass().add("assurance-gridpane");
            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);

            vBoxContent.getChildren().add(gp);
        }
    }


    private void creerAssurance(ActionEvent event) {
        Stage s = new Stage();
        JfxUtil.showWindow(s, VueNewAssurance.class);
    }

    @FXML
    private void retourImmeubles(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    void attribuerAssurance(Bien bien, Assurance assurance) {
        if (assurance == null) {
            JfxUtil.displayError("Erreur", "L'assurance sélectionnée est invalide.");
            return;
        }

        try {
            assurance.attribuerUneAssurance(bien);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Attribution réussie");
            alert.setContentText("L'assurance a été attribuée avec succès au bien !");
            alert.showAndWait();

            // Actualisation de la liste des assurances
            afficheAssurances();

        } catch (Assurance.AssuranceException assuranceException) {
            // Affichage de l'erreur avec des détails pertinents
            JfxUtil.displayError(
                    "Erreur lors de l'attribution de l'assurance",
                    "ID Bien : " + bien.getIdBien() + "\nID Assurance : " + assurance.getIdAssurance() +
                            "\nOn ne peut pas associer 2 assurances différentes \nsur un même bien la même année. "
            );

        }
    }

    private void deleteAssurance(int id) throws Assurance.AssuranceException {
        try {
            assurances.get(id).delete();
            afficheAssurances();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression réussie");
            alert.setHeaderText(null);
            alert.setContentText("L'assurance a été supprimée avec succès.");
            alert.showAndWait();
        } catch (Assurance.AssuranceException e) {
            JfxUtil.displayError("Erreur lors de la suppression de l'assurance", e.getMessage());
        }
        afficheAssurances();
    }

}