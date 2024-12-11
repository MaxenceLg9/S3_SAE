package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.TypeContrat;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewAssurance {

    @FXML
    private TextField fieldProtectionJuridique;
    @FXML
    private TextField fieldQuotiteJuridique;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;

    @FXML
    private List<TextField> fieldsAssurance;

    // Le constructeur est optionnel dans un contrôleur JavaFX, mais si nécessaire, il pourrait être ajouté ici
    public CtrlNewAssurance() {
        // Ce constructeur est généralement utilisé pour des initialisations autres que JavaFX
        // Par exemple, injection de dépendances ou initialisation des valeurs si cela est requis
        System.out.println("Constructeur de CtrlNewAssurance appelé");
    }

    @FXML
    public void initialize() {
        fieldSetup();
        setupComboBox();
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsAssurance = new ArrayList<>() {
            {
                add(fieldProtectionJuridique);
                add(fieldQuotiteJuridique);
            }
        };
    }

    private void setFieldsPromptText() {
        fieldProtectionJuridique.setPromptText("Protection Juridique ");
        fieldQuotiteJuridique.setPromptText("Quotité Juridique (en %)");
    }

    private void setupComboBox() {
        comboTypeContrat.getItems().addAll(TypeContrat.values());
        comboTypeContrat.setPromptText("Type de Contrat");
    }

    @FXML
    public void ajouterAssurance(ActionEvent event) {
        if (fieldsNotEmpty()) {
            try {
                float protectionJuridique = Float.parseFloat(fieldProtectionJuridique.getText());
                float quotiteJuridique = Float.parseFloat(fieldQuotiteJuridique.getText());
                TypeContrat typeContrat = comboTypeContrat.getValue();

                if (typeContrat == null) {
                    alertError("Type de contrat manquant", "Veuillez sélectionner un type de contrat.");
                    return;
                }

                // Enregistrement de l'assurance dans la base de données
                Assurance assurance = new Assurance(typeContrat);
                assurance.setProtectionJuridique(protectionJuridique);
                assurance.setQuotiteJurisprudence(quotiteJuridique);
                assurance.save();

            } catch (NumberFormatException e) {
                alertError("Format des champs invalide", "Veuillez saisir des valeurs numériques pour les champs appropriés.");
            } catch (Assurance.AssuranceException e) {
                e.printStackTrace();
                alertError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout de l'assurance.");
            }
        } else {
            alertFieldsEmpty();
        }

        try {
            // Création d'une nouvelle fenêtre
            Stage stage = new Stage();
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");
            stage.setWidth(1300);
            stage.setHeight(900);
            stage.setResizable(false);

            // Fermeture de la fenêtre actuelle
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Affichage de la nouvelle fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertFieldsEmpty() {
        alertError("Champs vides", "Veuillez remplir tous les champs avant de valider.");
    }

    private void alertError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean fieldsNotEmpty() {
        for (TextField textField : fieldsAssurance) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void annuler(ActionEvent actionEvent) {
        try {
            // Création d'une nouvelle fenêtre
            Stage stage = new Stage();
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");
            stage.setWidth(1300);
            stage.setHeight(900);
            stage.setResizable(false);

            // Fermeture de la fenêtre actuelle
            Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            // Affichage de la nouvelle fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
