package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;

import java.util.ArrayList;
import java.util.List;

public class CtrlCharges {

    @FXML
    private DatePicker dateCharge;
    @FXML
    private TextField fieldProvision;
    @FXML
    private TextField fieldNouvelIndice;
    @FXML
    private TextField fieldPartieFixe;
    @FXML
    private TextField fieldPartieVariable;
    @FXML
    private TextField fieldMontantEntretien;
    @FXML
    private TextField fieldMontantOrdures;
    @FXML
    private TextField fieldMontantElectricite;
    @FXML
    private ComboBox<String> comboTypeCharges;

    private int idBail;

    @FXML
    public void initialize() {
        setupComboBox();

        // Listener pour activer/désactiver les champs selon le type de charges
        comboTypeCharges.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFields();
            switch (newValue) {
                case "Eau":
                    setFieldVisibility(true, fieldNouvelIndice, fieldPartieFixe, fieldPartieVariable);
                    break;
                case "Entretien":
                    setFieldVisibility(true, fieldMontantEntretien);
                    break;
                case "Ordures Ménagères":
                    setFieldVisibility(true, fieldMontantOrdures);
                    break;
                case "Électricité":
                    setFieldVisibility(true, fieldMontantElectricite);
                    break;
                default:
                    break;
            }
        });

        fieldSetup();
    }

    public void setIdBail(Stage stage) {
        Object id = stage.getProperties().get("bail");
        if (id instanceof Integer) {
            this.idBail = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bail' manquante ou incorrecte.");
        }
    }

    private void setupComboBox() {
        // Pré-sélectionner une option pour éviter un champ vide au lancement
        comboTypeCharges.getSelectionModel().selectFirst();
        comboTypeCharges.getItems().setAll("Eau", "Entretien", "Ordures Ménagères", "Électricité");
    }

    private void resetFields() {
        fieldNouvelIndice.clear();
        fieldPartieFixe.clear();
        fieldPartieVariable.clear();
        fieldMontantEntretien.clear();
        fieldMontantOrdures.clear();
        fieldMontantElectricite.clear();
        setFieldVisibility(false, fieldNouvelIndice, fieldPartieFixe, fieldPartieVariable, fieldMontantEntretien, fieldMontantOrdures, fieldMontantElectricite);
    }

    private void setFieldVisibility(boolean visible, TextField... fields) {
        for (TextField field : fields) {
            field.setVisible(visible);
            field.setManaged(visible);
        }
    }

    private void fieldSetup() {
        fieldProvision.setPromptText("Provision sur charge (€)");
        fieldNouvelIndice.setPromptText("Nouvel indice (eau)");
        fieldPartieFixe.setPromptText("Partie fixe (€)");
        fieldPartieVariable.setPromptText("Partie variable (€)");
        fieldMontantEntretien.setPromptText("Montant entretien (€)");
        fieldMontantOrdures.setPromptText("Montant ordures ménagères (€)");
        fieldMontantElectricite.setPromptText("Montant électricité (€)");

        applyNumericValidation(fieldProvision, fieldNouvelIndice, fieldPartieFixe, fieldPartieVariable, fieldMontantEntretien, fieldMontantOrdures, fieldMontantElectricite);
    }

    private void applyNumericValidation(TextField... fields) {
        for (TextField field : fields) {
            field.setTextFormatter(new TextFormatter<>(change ->
                    change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null));
        }
    }

    @FXML
    public void ajouterCharge(ActionEvent event) {
        if (!fieldsNotEmpty()) {
            alertFieldsEmpty();
            return;
        }

        try {
            String typeCharge = comboTypeCharges.getValue();
            double provision = Double.parseDouble(fieldProvision.getText());

            // Gestion des données spécifiques au type de charges
            switch (typeCharge) {
                case "Eau":
                    double nouvelIndice = Double.parseDouble(fieldNouvelIndice.getText());
                    double partieFixe = Double.parseDouble(fieldPartieFixe.getText());
                    double partieVariable = Double.parseDouble(fieldPartieVariable.getText());
                    System.out.printf("Type: %s, Provision: %.2f, Nouvel Indice: %.2f, Partie Fixe: %.2f, Partie Variable: %.2f%n",
                            typeCharge, provision, nouvelIndice, partieFixe, partieVariable);
                    break;

                case "Entretien":
                    double montantEntretien = Double.parseDouble(fieldMontantEntretien.getText());
                    System.out.printf("Type: %s, Provision: %.2f, Montant Entretien: %.2f%n",
                            typeCharge, provision, montantEntretien);
                    break;

                case "Ordures Ménagères":
                    double montantOrdures = Double.parseDouble(fieldMontantOrdures.getText());
                    System.out.printf("Type: %s, Provision: %.2f, Montant Ordures: %.2f%n",
                            typeCharge, provision, montantOrdures);
                    break;

                case "Électricité":
                    double montantElectricite = Double.parseDouble(fieldMontantElectricite.getText());
                    System.out.printf("Type: %s, Provision: %.2f, Montant Électricité: %.2f%n",
                            typeCharge, provision, montantElectricite);
                    break;

                default:
                    throw new IllegalArgumentException("Type de charge inconnu.");
            }

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Charge ajoutée avec succès !");
            successAlert.showAndWait();

        } catch (NumberFormatException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Format de saisie incorrect");
            errorAlert.setContentText("Veuillez saisir des valeurs numériques valides.");
            errorAlert.showAndWait();
        }
    }

    private void alertFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs et sélectionner une date.");
        alert.showAndWait();
    }

    private boolean fieldsNotEmpty() {
        if (fieldProvision.getText().isEmpty() || dateCharge.getValue() == null) {
            return false;
        }

        switch (comboTypeCharges.getValue()) {
            case "Eau":
                return !fieldNouvelIndice.getText().isEmpty()
                        && !fieldPartieFixe.getText().isEmpty()
                        && !fieldPartieVariable.getText().isEmpty();
            case "Entretien":
                return !fieldMontantEntretien.getText().isEmpty();
            case "Ordures Ménagères":
                return !fieldMontantOrdures.getText().isEmpty();
            case "Électricité":
                return !fieldMontantElectricite.getText().isEmpty();
            default:
                return false;
        }
    }

    @FXML
    public void annuler(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Accueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueAccueil.class);
    }
}
