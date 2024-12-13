package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.mpvm.saeimmobilier.modele.Proprietaire;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CtrlInscription {

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
    private TextField fieldCodePostal,fieldAdresse,fieldVille,fieldPrenom,fieldNom,fieldMail,fieldTelephone;


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

    private void alertInvalidEmail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Adresse e-mail invalide");
        alert.setContentText("Veuillez saisir une adresse e-mail valide.");
        alert.showAndWait();
    }

    private void alertInvalidPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Mot de passe invalide");
        alert.setContentText("Le mot de passe doit contenir au moins 8 caractères.");
        alert.showAndWait();
    }

    @FXML
    public void Valider(ActionEvent event) {
        fieldNewPassword.setText(fieldNewPasswordVisible.getText());
        fieldConfirmation.setText(fieldConfirmationVisible.getText());
        fieldConfirmationVisible.setText(fieldConfirmation.getText());
        fieldNewPasswordVisible.setText(fieldNewPassword.getText());
        if (fieldsNotEmpty()) {
            if (!isValidEmail(fieldMail.getText())) {
                alertInvalidEmail();
                return;
            }

            if (!isValidPassword(fieldNewPassword.getText())) {
                alertInvalidPassword();
                return;
            }

            if (MDPIdentique()) {
                try {
                    new Proprietaire(fieldNom.getText(),fieldPrenom.getText(),fieldTelephone.getText(),fieldMail.getText(),fieldNewPassword.getText()).save();
                } catch (Proprietaire.ProprietaireException proprietaireException) {
                    proprietaireException.getSqlException().printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la sauvegarde");
                    alert.setContentText(proprietaireException.getMessage());
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.\\w{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    @FXML
    private void setupVisibility(ActionEvent actionEventS) {
        if (checkBoxVisibilite.isSelected()) {
            // Show passwords in plain text (visible TextField)
            fieldNewPasswordVisible.setText(fieldNewPassword.getText());
            fieldNewPasswordVisible.setVisible(true);
            fieldNewPassword.setVisible(false);

            fieldConfirmationVisible.setText(fieldConfirmation.getText());
            fieldConfirmationVisible.setVisible(true);
            fieldConfirmation.setVisible(false);
        } else {
            // Hide plain text fields and restore PasswordField
            fieldNewPassword.setText(fieldNewPasswordVisible.getText());
            fieldNewPassword.setVisible(true);
            fieldNewPasswordVisible.setVisible(false);

            fieldConfirmation.setText(fieldConfirmationVisible.getText());
            fieldConfirmation.setVisible(true);
            fieldConfirmationVisible.setVisible(false);
        }
    }
}
