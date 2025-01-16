package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class CtrlCharges {

    @FXML
    private DatePicker dateCharge;
    @FXML
    private TextField fieldProvision;
    @FXML
    private TextField fieldNouvelIndice;
    @FXML
    private TextField fieldAncienIndice;
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
        resetFields();
        comboTypeCharges.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFields();
            switch (newValue) {
                case "Provision sur Charge":
                    fieldProvision.setDisable(false);
                    break;
                case "Eau":
                    fieldNouvelIndice.setDisable(false);
                    fieldAncienIndice.setDisable(false);
                    fieldPartieFixe.setDisable(false);
                    fieldPartieVariable.setDisable(false);
                    break;
                case "Entretien":
                    fieldMontantEntretien.setDisable(false);
                    break;
                case "Ordures Ménagères":
                    fieldMontantOrdures.setDisable(false);
                    break;
                case "Électricité":
                    fieldMontantElectricite.setDisable(false);
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
        comboTypeCharges.getItems().setAll("Provision sur charge", "Eau", "Entretien", "Ordures Ménagères", "Électricité");
        comboTypeCharges.getSelectionModel().selectFirst();
    }

    private void resetFields() {
        fieldProvision.clear();
        fieldNouvelIndice.clear();
        fieldPartieFixe.clear();
        fieldPartieVariable.clear();
        fieldMontantEntretien.clear();
        fieldMontantOrdures.clear();
        fieldMontantElectricite.clear();
        fieldProvision.setDisable(true);
        fieldNouvelIndice.setDisable(true);
        fieldPartieFixe.setDisable(true);
        fieldPartieVariable.setDisable(true);
        fieldMontantEntretien.setDisable(true);
        fieldMontantOrdures.setDisable(true);
        fieldMontantElectricite.setDisable(true);
        fieldAncienIndice.setDisable(true);

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
            LocalDate date = dateCharge.getValue();
            Date sqlDate = Date.valueOf(date);

            switch (typeCharge) {
                case "Provision sur charge":
                    Charges chargeProvision = new Charges.ProvisionCharge(sqlDate );
                    ((Charges.ProvisionCharge) chargeProvision).setProvision(Float.parseFloat(fieldProvision.getText()));
                    chargeProvision.setIdBail(idBail);
                    chargeProvision.save();
                    break;

                case "Eau":
                    Charges chargeEau = new Charges.ChargeEau(sqlDate);
                    ((Charges.ChargeEau) chargeEau).setNouvelIndice(Integer.parseInt(fieldNouvelIndice.getText()));

                    ((Charges.ChargeEau) chargeEau).setAncienIndice(Integer.parseInt(fieldAncienIndice.getText()));
                    ((Charges.ChargeEau) chargeEau).setPartieFixe(Float.parseFloat(fieldPartieFixe.getText()));
                    ((Charges.ChargeEau) chargeEau).setPartieVariable(Float.parseFloat(fieldPartieVariable.getText()));
                    ((Charges.ChargeEau) chargeEau).calculerMontant();

                    chargeEau.setIdBail(idBail);
                    chargeEau.save();
                    break;

                case "Entretien":
                    Charges chargeEntretien = new Charges.ChargeEntretien(sqlDate);
                    chargeEntretien.setMontant(Float.parseFloat(fieldMontantEntretien.getText()));
                    chargeEntretien.setIdBail(idBail);
                    chargeEntretien.save();
                    break;

                case "Ordures Ménagères":

                    Charges chargeOrdures = new Charges.ChargeOrduresMenageres(sqlDate);
                    chargeOrdures.setMontant(Float.parseFloat(fieldMontantOrdures.getText()));
                    chargeOrdures.setIdBail(idBail);
                    chargeOrdures.save();
                    break;

                case "Électricité":
                    Charges chargeElectricite = new Charges.ChargeElectricite(sqlDate);
                    chargeElectricite.setMontant(Float.parseFloat(fieldMontantElectricite.getText()));
                    chargeElectricite.setIdBail(idBail);
                    chargeElectricite.save();
                    break;

                default:
                    throw new IllegalArgumentException("Type de charge inconnu.");
            }

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Charge ajoutée avec succès !");
            successAlert.showAndWait();

        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur lors de l'ajout de la charge");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
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
        if (dateCharge.getValue() == null) {
            return false;
        }

        switch (comboTypeCharges.getValue()) {
            case "Provision sur charge":
                return !fieldProvision.getText().isEmpty();
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


}
