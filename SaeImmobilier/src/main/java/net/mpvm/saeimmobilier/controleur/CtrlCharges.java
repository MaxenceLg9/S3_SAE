package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.sql.Date;
import java.time.LocalDate;


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
        resetFields();
        fieldSetup();
        setupComboBox();
        resetFields();
        comboTypeCharges.valueProperty().addListener((obs, oldValue, newValue) -> {
            switch (newValue) {
                case Charges.PROVISION_SUR_CHARGE:
                    resetFields();
                    fieldProvision.setDisable(false);
                    break;
                case Charges.EAU:
                    resetFields();
                    fieldNouvelIndice.setDisable(false);
                    fieldAncienIndice.setDisable(false);
                    fieldPartieFixe.setDisable(false);
                    fieldPartieVariable.setDisable(false);
                    break;
                case Charges.ENTRETIEN:
                    resetFields();
                    fieldMontantEntretien.setDisable(false);
                    break;
                case Charges.ORDURES_MENAGERES:
                    resetFields();
                    fieldMontantOrdures.setDisable(false);
                    break;
                case Charges.ELECTRICITE:
                    resetFields();
                    fieldMontantElectricite.setDisable(false);
                    break;
                default:
                    break;
            }
        });
        dateCharge.sceneProperty().addListener((observable, oldScene, newScene) -> {
            System.out.println("Scène modifiée : " + newScene);
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    System.out.println("Fenêtre détectée : " + newWindow);
                    if (newWindow instanceof Stage stage) {
                        System.out.println("Stage trouvé !");
                        setIdBail(stage);
                    } else {
                        System.out.println("Pas de stage détecté.");
                    }
                });
            }
        });


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
        comboTypeCharges.getItems().setAll(Charges.PROVISION_SUR_CHARGE, Charges.EAU, Charges.ENTRETIEN, Charges.ORDURES_MENAGERES, Charges.ELECTRICITE);
        comboTypeCharges.setValue(null);
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
        fieldAncienIndice.setPromptText("Ancien indice (eau)");
        fieldPartieFixe.setPromptText("Partie fixe (€)");
        fieldPartieVariable.setPromptText("Partie variable (€)");
        fieldMontantEntretien.setPromptText("Montant entretien (€)");
        fieldMontantOrdures.setPromptText("Montant ordures ménagères (€)");
        fieldMontantElectricite.setPromptText("Montant électricité (€)");

        applyNumericValidation(fieldProvision, fieldNouvelIndice, fieldPartieFixe, fieldPartieVariable, fieldMontantEntretien, fieldMontantOrdures, fieldMontantElectricite,fieldAncienIndice);
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
        System.out.println(idBail);

        try {
            String typeCharge = comboTypeCharges.getValue();
            LocalDate date = dateCharge.getValue();
            Date sqlDate = Date.valueOf(date);

            switch (typeCharge) {
                case Charges.PROVISION_SUR_CHARGE:
                    Charges chargeProvision = new Charges.ProvisionCharge(sqlDate );
                    chargeProvision.setMontant(Float.parseFloat(fieldProvision.getText()));
                    chargeProvision.setIdBail(idBail);
                    chargeProvision.save();
                    break;

                case Charges.EAU:
                    Charges.ChargeEau chargeEau = getChargeEau(sqlDate);
                    chargeEau.save();
                    break;

                case Charges.ENTRETIEN:
                    Charges chargeEntretien = new Charges.ChargeEntretien(sqlDate);
                    chargeEntretien.setMontant(Float.parseFloat(fieldMontantEntretien.getText()));
                    chargeEntretien.setIdBail(idBail);
                    chargeEntretien.save();
                    break;

                case Charges.ORDURES_MENAGERES:

                    Charges chargeOrdures = new Charges.ChargeOrduresMenageres(sqlDate);
                    chargeOrdures.setMontant(Float.parseFloat(fieldMontantOrdures.getText()));
                    chargeOrdures.setIdBail(idBail);

                    chargeOrdures.save();
                    break;

                case Charges.ELECTRICITE:
                    Charges chargeElectricite = new Charges.ChargeElectricite(sqlDate);
                    chargeElectricite.setMontant(Float.parseFloat(fieldMontantElectricite.getText()));
                    chargeElectricite.setIdBail(idBail);
                    chargeElectricite.save();
                    break;

                default:
                    throw new IllegalArgumentException("Type de charge inconnu.");
            }
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", null, "Charge ajoutée avec succès !");
        } catch (Exception e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de la charge", e.getMessage());
            e.printStackTrace();
        }
    }

    private Charges.ChargeEau getChargeEau(Date sqlDate) {
        Charges.ChargeEau chargeEau = new Charges.ChargeEau(sqlDate);
        chargeEau.setNouvelIndice(Integer.parseInt(fieldNouvelIndice.getText()));

        chargeEau.setAncienIndice(Integer.parseInt(fieldAncienIndice.getText()));
        chargeEau.setPartieFixe(Float.parseFloat(fieldPartieFixe.getText()));
        chargeEau.setPartieVariable(Float.parseFloat(fieldPartieVariable.getText()));
        chargeEau.calculerMontant();

        chargeEau.setIdBail(idBail);
        return chargeEau;
    }

    private void alertFieldsEmpty() {
        JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs et sélectionner une date.");
    }

    private boolean fieldsNotEmpty() {
        if (dateCharge.getValue() == null) {
            return false;
        }

        return switch (comboTypeCharges.getValue()) {
            case Charges.PROVISION_SUR_CHARGE -> !fieldProvision.getText().isEmpty();
            case Charges.EAU -> !fieldNouvelIndice.getText().isEmpty()
                    && !fieldPartieFixe.getText().isEmpty()
                    && !fieldPartieVariable.getText().isEmpty();
            case Charges.ENTRETIEN -> !fieldMontantEntretien.getText().isEmpty();
            case Charges.ORDURES_MENAGERES -> !fieldMontantOrdures.getText().isEmpty();
            case Charges.ELECTRICITE -> !fieldMontantElectricite.getText().isEmpty();
            default -> false;
        };
    }

    @FXML
    public void annuler(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
