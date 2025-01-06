package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlAttribuerAssurance {

    @FXML
    public VBox vBoxContent;

    private Map<Integer, Assurance> assurances;

    public void initialize() {
        afficheAssurances();
    }

    private void afficheAssurances() {
        try {
            assurances = Assurance.findAll().stream().collect(Collectors.toMap(Assurance::getIdAssurance, Function.identity()));
        } catch (Assurance.AssuranceException assuranceException) {
            JfxUtil.displayError("Erreur lors de la récupération des assurances", "Vérifiez votre connexion Internet");
        }

        vBoxContent.getChildren().clear();

        for (Assurance a : assurances.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(150);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1, col1);

            Label idAssurance = new Label("Assurance " + a.getIdAssurance());
            Label protectionJuridique = new Label("Protection Juridique " + a.getProtectionJuridique());
            Label quotiteJuridique = new Label("Quotité Juridique " + a.getQuotiteJurisprudence());
            Label prime = new Label("Prime " + a.getPrime());
            Label totalPrime = new Label("Total Prime " + a.getTotalPrime());
            Label augmentation = new Label("Augmentation " + a.getAugmentationAnnuelle() + " %");
            Label typeContrat = new Label("Type de Contrat " + a.getTypeContrat());
            Label annee = new Label("Année " + a.getAnnee());
            Button deleteButton = new Button("Supprimer l'assurance");
            Button chooseButton = new Button("Choisir");

            deleteButton.setOnAction(event -> askForDelete(a.getIdAssurance()));

            chooseButton.setOnAction(event -> {
                // Action future pour le bouton "Choisir"
                System.out.println("Assurance choisie : " + a.getIdAssurance());
            });

            // Styles CSS pour les labels et boutons
            idAssurance.getStyleClass().add("assurance-label");
            idAssurance.getStyleClass().add("assurance-title");
            protectionJuridique.getStyleClass().add("assurance-label");
            quotiteJuridique.getStyleClass().add("assurance-label");
            prime.getStyleClass().add("assurance-label");
            totalPrime.getStyleClass().add("assurance-label");
            augmentation.getStyleClass().add("assurance-label");
            typeContrat.getStyleClass().add("assurance-label");
            annee.getStyleClass().add("assurance-label");
            annee.getStyleClass().add("assurance-title");
            deleteButton.getStyleClass().add("assurance-button");
            deleteButton.getStyleClass().add("button-supprimer");
            chooseButton.getStyleClass().add("assurance-button");
            chooseButton.getStyleClass().add("button-valider");

            // Ajout des labels et boutons au GridPane
            gp.add(idAssurance, 0, 0);
            gp.add(protectionJuridique, 0, 1);
            gp.add(quotiteJuridique, 0, 2);
            gp.add(prime, 1, 0);
            gp.add(totalPrime, 1, 1);
            gp.add(augmentation, 1, 2);
            gp.add(typeContrat, 1, 3);
            gp.add(annee, 2, 0);
            gp.add(deleteButton, 2, 3);
            gp.add(chooseButton, 2, 2);

            gp.setAlignment(Pos.TOP_CENTER);

            GridPane.setHalignment(idAssurance, HPos.LEFT);
            GridPane.setHalignment(protectionJuridique, HPos.LEFT);
            GridPane.setHalignment(quotiteJuridique, HPos.LEFT);
            GridPane.setHalignment(prime, HPos.LEFT);
            GridPane.setHalignment(totalPrime, HPos.LEFT);
            GridPane.setHalignment(augmentation, HPos.LEFT);
            GridPane.setHalignment(typeContrat, HPos.LEFT);
            GridPane.setHalignment(annee, HPos.LEFT);
            GridPane.setValignment(idAssurance, VPos.CENTER);

            gp.getStyleClass().add("assurance-gridpane");
            gp.setHgap(10);
            gp.setVgap(5);

            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);
            int bienId = 0;
            chooseButton.setOnAction(event -> attribuerAssurance(bienId, a));

            vBoxContent.getChildren().add(gp);
        }
    }
    public void attribuerAssurance(int idBien, Assurance nouvelleAssurance) {
        try {
            // Récupérer le bien concerné
            Bien bien = Bien.findById(idBien);
            if (bien == null) {
                throw new Exception("Le bien avec l'ID spécifié n'existe pas.");
            }

            // Récupérer l'assurance actuelle du bien (si elle existe via Optional)
            Optional<Assurance> assuranceActuelleOpt = bien.getAssurance();
            Assurance assuranceActuelle = assuranceActuelleOpt.orElse(null);

            // Vérification des conditions pour associer la nouvelle assurance
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