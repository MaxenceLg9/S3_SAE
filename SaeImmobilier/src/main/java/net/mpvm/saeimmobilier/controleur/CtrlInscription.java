package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueBienvenue;
import net.mpvm.saeimmobilier.vue.VueConnexion;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CtrlInscription {

    @FXML
    public Button btnValider;

    @FXML
    public Button btnAnnuler;

    @FXML
    public PasswordField fieldConfirmPassword;

    @FXML
    public TextField fieldConfirmPasswordVisible;

    @FXML
    public TextField fieldPasswordVisible;

    @FXML
    public PasswordField fieldPassword;

    @FXML
    public CheckBox checkBoxVisibilite;

    @FXML
    private TextField fieldMail;


    private ArrayList<TextField> fieldsMDP;

    @FXML
    public void initialize() {
        assert fieldPassword != null : "fieldNewPassword is null";
        assert fieldPasswordVisible != null : "fieldNewPasswordVisible is null";
        assert fieldConfirmPassword != null : "fieldConfirmation is null";
        assert fieldConfirmPasswordVisible != null : "fieldConfirmationVisible is null";
        try{
            if (!Proprietaire.findAll().isEmpty()){
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Il existe déjà un propriétaire",
                        "Un propriétaire est déjà présent, essayez avec les informations déjà enregistrées");
                Platform.runLater(() -> JfxUtil.showWindow(((Stage) fieldPassword.getScene().getWindow()), VueConnexion.class));
            }
        } catch (Proprietaire.ProprietaireException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Erreur lors de la récupération des données",
                    "Vérifier votre connexion");
        }
        // Initial setup
        fieldSetup();
        checkBoxVisibilite.setSelected(false); // Default: password hidden
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsMDP = new ArrayList<>() {{
            add(fieldMail);
            add(fieldPassword);
            add(fieldConfirmPassword);
        }};

        fieldPassword.setOnAction(_ -> fieldPasswordVisible.setText(fieldPassword.getText()));
        fieldPasswordVisible.setOnAction(_ -> fieldPassword.setText(fieldPasswordVisible.getText()));
        fieldConfirmPassword.setOnAction(_ -> fieldConfirmPasswordVisible.setText(fieldConfirmPassword.getText()));
        fieldConfirmPasswordVisible.setOnAction(_ -> fieldConfirmPassword.setText(fieldConfirmPasswordVisible.getText()));
        // Hide visible fields initially
        fieldPasswordVisible.setVisible(false);
        fieldConfirmPasswordVisible.setVisible(false);
    }

    private void setFieldsPromptText() {
        fieldMail.setPromptText("Adresse Mail");
        fieldPassword.setPromptText("Nouveau Mot de Passe");
        fieldConfirmPassword.setPromptText("Confirmation");
    }

    @FXML
    public void Valider(ActionEvent event) {
        if (fieldsNotEmpty()) {
            if (!isValidEmail(fieldMail.getText())) {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Adresse e-mail invalide",
                        "Veuillez entrer une adresse e-mail valide (exemple : utilisateur@domaine.com).");
                return;
            }

            if (!isValidPassword(fieldPassword.getText())) {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Mot de passe invalide",
                        "Votre mot de passe doit contenir au moins :\n"
                                + "- Une majuscule\n"
                                + "- Une minuscule\n"
                                + "- Un chiffre\n"
                                + "- Un caractère spécial (!@#$%^&*)\n"
                                + "- Et avoir une longueur minimale de 8 caractères.");
                return;
            }

            if (MDPIdentique()) {
                try {
                    new Proprietaire(fieldMail.getText(), fieldPassword.getText()).save();
                    JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                            "Succès",
                            "Inscription réussie",
                            "Vous êtes maintenant inscrit ! Vous pouvez passer à la connexion");
                    JfxUtil.showWindow(((Stage) fieldPassword.getScene().getWindow()), VueConnexion.class);
                } catch (Proprietaire.ProprietaireException proprietaireException) {
                    JfxUtil.setAlert(Alert.AlertType.ERROR,
                            "Erreur",
                            "Erreur lors de la sauvegarde",
                            proprietaireException.getMessage());
                }
            } else {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Mots de passe non identiques",
                        "Les mots de passe ne correspondent pas ! Veuillez les vérifier.");
            }
        } else {
            JfxUtil.setAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Champs vides",
                    "Vous devez remplir tous les champs pour vous inscrire.");
        }
    }


    @FXML
    public void Annuler(ActionEvent event) {
        JfxUtil.showWindow(((Stage) fieldPassword.getScene().getWindow()), VueBienvenue.class);
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
        return fieldPassword.getText().equals(fieldConfirmPassword.getText());
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    private boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$", password);
    }

    @FXML
    private void setupVisibility(ActionEvent actionEventS) {
        if (checkBoxVisibilite.isSelected()) {
            // Show passwords in plain text (visible TextField)
            fieldPasswordVisible.setText(fieldPassword.getText());
            fieldPasswordVisible.setVisible(true);
            fieldPassword.setVisible(false);

            fieldConfirmPasswordVisible.setText(fieldConfirmPassword.getText());
            fieldConfirmPasswordVisible.setVisible(true);
            fieldConfirmPassword.setVisible(false);
        } else {
            // Hide plain text fields and restore PasswordField
            fieldPassword.setText(fieldPasswordVisible.getText());
            fieldPassword.setVisible(true);
            fieldPasswordVisible.setVisible(false);

            fieldConfirmPassword.setText(fieldConfirmPasswordVisible.getText());
            fieldConfirmPassword.setVisible(true);
            fieldConfirmPasswordVisible.setVisible(false);
        }
    }
}
