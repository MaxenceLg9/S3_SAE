package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.mpvm.saeimmobilier.modele.Proprietaire;


import java.util.ArrayList;

public class CtrlAuthentification {

    @FXML
    public Button btnValider;

    @FXML
    public Button btnAnnuler;
    @FXML
    public PasswordField fieldConfirmation;
    @FXML
    public TextField fieldConfirmationVisible;
    @FXML
    public TextField fieldNewPasswordVisible;
    @FXML
    public PasswordField fieldNewPassword;
    @FXML
    public CheckBox checkBoxVisibilite;
    @FXML
    public TextField fieldMail;

    private ArrayList<TextField> fieldsMDP;

    @FXML
    public void initialize() {
        assert fieldNewPassword != null : "fieldNewPassword is null";
        assert fieldNewPasswordVisible != null : "fieldNewPasswordVisible is null";
        assert fieldConfirmation != null : "fieldConfirmation is null";
        assert fieldConfirmationVisible != null : "fieldConfirmationVisible is null";

        // Initial setup
        fieldSetup();
        checkBoxVisibilite.setSelected(false); // Default: password hidden
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsMDP = new ArrayList<>() {{
            add(fieldMail);
            add(fieldNewPassword);
            add(fieldConfirmation);
        }};

        // Hide visible fields initially
        fieldNewPasswordVisible.setVisible(false);
        fieldConfirmationVisible.setVisible(false);
    }

    private void setFieldsPromptText() {
        fieldMail.setPromptText("Adresse Mail");
        fieldNewPassword.setPromptText("Nouveau Mot de Passe");
        fieldConfirmation.setPromptText("Confirmation");
    }

    private void alertFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs");
        alert.showAndWait();
    }

    @FXML
    public void Valider(ActionEvent event) {
        if (fieldsNotEmpty()) {
            if (MDPIdentique()) {
                try {
                    new Proprietaire(fieldMail.getText(), fieldNewPassword.getText()).save();
                } catch (Proprietaire.ProprietaireException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la sauvegarde");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Mots de passe non identiques");
                alert.setContentText("Les mots de passe ne correspondent pas !");
                alert.showAndWait();
            }
        } else {
            alertFieldsEmpty();
        }
    }

    @FXML
    public void Annuler(ActionEvent event) {
        // Clear all fields
        fieldMail.clear();
        fieldNewPassword.clear();
        fieldConfirmation.clear();
        fieldNewPasswordVisible.clear();
        fieldConfirmationVisible.clear();
        checkBoxVisibilite.setSelected(false);
    }

    private boolean fieldsNotEmpty() {
        for (TextField textField : fieldsMDP) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean MDPIdentique() {
        return fieldNewPassword.getText().equals(fieldConfirmation.getText());
    }

    @FXML
    private void setupVisibility(javafx.event.ActionEvent actionEventS) {
        if (checkBoxVisibilite.isSelected()) {
            // Afficher les mots de passe en texte clair (TextField visible)
            fieldNewPasswordVisible.setText(fieldNewPassword.getText());
            System.out.println(fieldNewPasswordVisible.getText());// Copier le texte
            fieldNewPasswordVisible.setVisible(true);
            fieldNewPassword.setVisible(false);

            fieldConfirmationVisible.setText(fieldConfirmation.getText()); // Copier le texte
            fieldConfirmationVisible.setVisible(true);
            fieldConfirmation.setVisible(false);
        } else {
            // Cacher les champs en texte clair et restaurer les PasswordField
            fieldNewPassword.setText(fieldNewPasswordVisible.getText()); // Copier le texte masqué dans le PasswordField
            fieldNewPassword.setVisible(true);
            fieldNewPasswordVisible.setVisible(false);


            fieldConfirmation.setText(fieldConfirmationVisible.getText()); // Copier le texte masqué dans le PasswordField
            fieldConfirmation.setVisible(true);
            fieldConfirmationVisible.setVisible(false);
        }
    }



}
