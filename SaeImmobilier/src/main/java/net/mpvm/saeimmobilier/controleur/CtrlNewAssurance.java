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

    public Button btnAnnuler;
    public Button btnAjouterAssurance;
    @FXML
    private TextField fieldProtectionJuridique;
    @FXML
    private TextField fieldQuotiteJuridique;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;

    @FXML
    private List<TextField> fieldsAssurance;

    // Le constructeur est optionnel dans un contrôleur JavaFX, mais si necessaire, il pourrait être ajoute ici
    public CtrlNewAssurance() {
        // Ce constructeur est generalement utilise pour des initialisations autres que JavaFX
        // Par exemple, injection de dependances ou initialisation des valeurs si cela est requis
        System.out.println("Constructeur de CtrlNewAssurance appele");
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
        fieldQuotiteJuridique.setPromptText("Quotite Juridique (en %)");
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
                    alertError("Type de contrat manquant", "Veuillez selectionner un type de contrat.");
                    return;
                }

                // Enregistrement de l'assurance dans la base de donnees
                Assurance assurance = new Assurance(typeContrat);
                assurance.setProtectionJuridique(protectionJuridique);
                assurance.setQuotiteJurisprudence(quotiteJuridique);
                assurance.save();

            } catch (NumberFormatException e) {
                alertError("Format des champs invalide", "Veuillez saisir des valeurs numeriques pour les champs appropries.");
            } catch (Assurance.AssuranceException e) {
                e.getSqlException().printStackTrace();
                alertError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout de l'assurance.");
            }
        } else {
            alertFieldsEmpty();
        }

        try {
            // Creation d'une nouvelle fenêtre
            Stage stage = new Stage();
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien",700,800);
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
            // Creation d'une nouvelle fenêtre
            Stage stage = new Stage();
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien",700,800);
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
    @FXML
    public void Annuler(ActionEvent actionEvent) {
    }
}
