package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.TypeContrat;

public class CtrlNewAssurance {

    @FXML
    private TextField txtProtectionJuridique;

    @FXML
    private TextField txtQuotiteJuridique;

    @FXML
    private TextField txtPrime;

    @FXML
    private TextField txtAugmentationAnnuelle;

    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;

    @FXML
    private Button btnAjouterAssurance;

    @FXML
    private void initialize() {
        // Populate the ComboBox with enum values
        comboTypeContrat.getItems().setAll(TypeContrat.values());

        // Select the first item by default (if applicable)
        if (!comboTypeContrat.getItems().isEmpty()) {
            comboTypeContrat.getSelectionModel().select(0);
        }
    }

    @FXML
    private void ajouterAssurance() {
        try {
            // Validate and retrieve inputs
            float protectionJuridique = Float.parseFloat(txtProtectionJuridique.getText());
            float quotiteJuridique = Float.parseFloat(txtQuotiteJuridique.getText());
            float prime = Float.parseFloat(txtPrime.getText());
            float augmentationAnnuelle = Float.parseFloat(txtAugmentationAnnuelle.getText());

            TypeContrat typeContrat = comboTypeContrat.getValue();
            if (typeContrat == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un type de contrat.");
            }

            // Create Assurance instance
            Assurance nouvelleAssurance = new Assurance(typeContrat);
            nouvelleAssurance.setProtectionJuridique(protectionJuridique);
            nouvelleAssurance.setQuotiteJurisprudence(quotiteJuridique);
            nouvelleAssurance.setPrime(prime);
            nouvelleAssurance.setAugmentationAnnuelle(augmentationAnnuelle);

            nouvelleAssurance.save();

            afficherMessage("Succès", "L'assurance a été ajoutée avec succès.", Alert.AlertType.INFORMATION);



        } catch (NumberFormatException e) {
            afficherMessage("Erreur", "Veuillez saisir des valeurs numériques valides.", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Assurance.AssuranceException e) {
            afficherMessage("Erreur", "Une erreur est survenue lors de l'ajout de l'assurance: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void afficherMessage(String titre, String contenu, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }


}
