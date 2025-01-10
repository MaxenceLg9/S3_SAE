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
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueImmeubles;
import net.mpvm.saeimmobilier.vue.VueNewAssurance;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlAttribuerAssurance {

    @FXML
    public VBox vBoxContent;

    private Map<Integer, Assurance> assurances;
    private int IdBien;

    public void initialize() {
        afficheAssurances();
    }
    public void setIdBien(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.IdBien = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }
    private void afficheAssurances() {
        try {
            assurances = Assurance.findAll().stream().collect(Collectors.toMap(Assurance::getIdAssurance, Function.identity()));
        } catch (Assurance.AssuranceException assuranceException) {
            JfxUtil.displayError("Erreur lors de la récupération des assurances", "Vérifiez votre connexion Internet");
        }
        Label titre = new Label("Assurances à Attribuer");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        titre.setAlignment(Pos.CENTER);

        vBoxContent.getChildren().clear();
        vBoxContent.getChildren().add(titre);
        Button retourImmeubles = new Button("Retour aux immeubles");
        retourImmeubles.setOnAction(event -> retourImmeubles(event));
        retourImmeubles.getStyleClass().add("button-supprimer");
        vBoxContent.getChildren().add(retourImmeubles);

        Button creerAssurance = new Button("Créer Assurance");
        creerAssurance.setOnAction(event -> creerAssurance(event));
        creerAssurance.getStyleClass().add("button-valider");
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
            Button deleteButton = new Button("Supprimer l'assurance");
            Button chooseButton = new Button("  Choisir  ");

            deleteButton.setOnAction(event -> askForDelete(a.getIdAssurance()));
            chooseButton.setOnAction(event -> attribuerAssurance(IdBien, a));

            // Styles CSS
            nomAssurance.getStyleClass().add("assurance-title");
            protectionJuridique.getStyleClass().add("assurance-label");
            prime.getStyleClass().add("assurance-label");
            typeContrat.getStyleClass().add("assurance-label");
            annee.getStyleClass().add("assurance-label");
            totalPrime.getStyleClass().add("assurance-label");
            deleteButton.getStyleClass().add("button-supprimer");
            chooseButton.getStyleClass().add("button-valider");

            // Ajout des labels et boutons au GridPane
            gp.add(nomAssurance, 0, 0);
            gp.add(annee, 0, 1);
            gp.add(prime, 1, 0);
            gp.add(typeContrat, 0, 2);
            gp.add(totalPrime, 1, 2);
            gp.add(protectionJuridique, 1, 1);
            gp.add(deleteButton, 2, 2);
            gp.add(chooseButton, 2, 0);

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
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        Stage s = new Stage();
        JfxUtil.showWindow(s, VueNewAssurance.class);
    }
    @FXML
    private void retourImmeubles(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        new VueImmeubles().start(new Stage());
    }


    public void attribuerAssurance(int idBien, Assurance nouvelleAssurance) {
        try {
            Bien bien = Bien.findById(idBien);
            if (bien == null) {
                throw new Exception("Le bien avec l'ID spécifié n'existe pas.");
            }

            Optional<Assurance> assuranceActuelleOpt = bien.getAssurance();
            Assurance assuranceActuelle = assuranceActuelleOpt.orElse(null);

            if (assuranceActuelle != null && nouvelleAssurance.getAnnee() != assuranceActuelle.getAnnee() + 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Assurance non valide");
                alert.setHeaderText("L'année de l'assurance choisie n'est pas valide.");
                alert.setContentText("Vous ne pouvez choisir qu'une assurance datant de l'année suivante.");
                alert.showAndWait();
                return;
            }

            // Calculer l'augmentation annuelle si une assurance actuelle existe
            double augmentationAnnuelle = 0;
            if (assuranceActuelle != null) {
                augmentationAnnuelle =
                        (nouvelleAssurance.getProtectionJuridique() - assuranceActuelle.getProtectionJuridique()) / 100.0;
            }

            // Mettre à jour l'augmentation annuelle dans la nouvelle assurance
            nouvelleAssurance.update(); // Sauvegarder la nouvelle assurance

            // Associer la nouvelle assurance au bien
            bien.setAssurance(nouvelleAssurance);
            bien.update(); // Sauvegarder le bien

            // Confirmation pour l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assurance attribuée");
            alert.setHeaderText("L'assurance a été attribuée au bien.");
            alert.setContentText("Augmentation annuelle : " + augmentationAnnuelle + " %");
            alert.showAndWait();

            // Rafraîchir l'affichage
            afficheAssurances();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue lors de l'attribution de l'assurance.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void askForDelete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de la suppression");
        alert.setHeaderText("Souhaitez-vous réellement supprimer cette assurance?");
        alert.setContentText("Cette action est irréversible");
        alert.showAndWait()
                .filter(r -> r.equals(ButtonType.OK))
                .ifPresent(r -> deleteAssurance(id));
    }

    private void deleteAssurance(int id) {
        try {
            assurances.get(id).delete();
        } catch (Assurance.AssuranceException e) {
            // TODO: handle exception with visual
        }
        afficheAssurances();
    }
}