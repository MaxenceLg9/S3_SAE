package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;
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
    public TextField fieldConfirmationVisible;

    @FXML
    public TextField fieldNewPasswordVisible;

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
        assert fieldNewPasswordVisible != null : "fieldNewPasswordVisible is null";
        assert fieldConfirmPassword != null : "fieldConfirmation is null";
        assert fieldConfirmationVisible != null : "fieldConfirmationVisible is null";

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

        // Hide visible fields initially
        fieldNewPasswordVisible.setVisible(false);
        fieldConfirmationVisible.setVisible(false);
    }

    private void setFieldsPromptText() {
        fieldMail.setPromptText("Adresse Mail");
        fieldPassword.setPromptText("Nouveau Mot de Passe");
        fieldConfirmPassword.setPromptText("Confirmation");
    }

    @FXML
    public void Valider(ActionEvent event) {
        fieldPassword.setText(fieldNewPasswordVisible.getText());
        fieldConfirmPassword.setText(fieldConfirmationVisible.getText());
        fieldConfirmationVisible.setText(fieldConfirmPassword.getText());
        fieldNewPasswordVisible.setText(fieldPassword.getText());
        if (fieldsNotEmpty()) {
            if (!isValidEmail(fieldMail.getText())) {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "L'adresse e-mail est invalide",
                        "L'adresse e-mail saisie n'est pas conforme");
                return;
            }

            if (!isValidPassword(fieldPassword.getText())) {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Le mot de passe est incorrect",
                        "Le mot de passe doit faire 8 caractères, contenir une majuscule, une minuscule, un chiffre et un caractère spécial au minimum");
                return;
            }

            try {
                if (Proprietaire.findAll().isEmpty()){
                    if (MDPIdentique()) {
                        try {
                            new Proprietaire(fieldMail.getText(), fieldPassword.getText()).save();
                            JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                                    "Succès",
                                    "Inscription réussie",
                                    "Vous êtes maintenant inscrit ! Vous pouvez passer à la connexion");
                            Stage stageActuel = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            stageActuel.close();
                            JfxUtil.showWindow(new Stage(), VueConnexion.class);
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
                                "Les mots de passe ne correspondent pas !");
                    }
                }else {
                    JfxUtil.setAlert(Alert.AlertType.ERROR,
                            "Erreur",
                            "Il existe déjà un propriétaire",
                            "Un propriétaire est déjà présent, essayez avec les informations déjà enregistrées");
                }
            } catch (Proprietaire.ProprietaireException e) {
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Erreur lors de la récupération des données",
                        "Vérifier votre connexion");
            }
        } else {
            JfxUtil.setAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Les champs sont vides",
                    "Vous devez remplir tout les champs si vous souhaitez vous inscrire");
        }
    }

    @FXML
    public void Annuler(ActionEvent event) {
        try {
            VueAccueil.showWindow(new Stage());
            Stage stageActuel = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stageActuel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.\\w{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && Pattern.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).*", password);

    }

    @FXML
    private void setupVisibility(ActionEvent actionEventS) {
        if (checkBoxVisibilite.isSelected()) {
            // Show passwords in plain text (visible TextField)
            fieldNewPasswordVisible.setText(fieldPassword.getText());
            fieldNewPasswordVisible.setVisible(true);
            fieldPassword.setVisible(false);

            fieldConfirmationVisible.setText(fieldConfirmPassword.getText());
            fieldConfirmationVisible.setVisible(true);
            fieldConfirmPassword.setVisible(false);
        } else {
            // Hide plain text fields and restore PasswordField
            fieldPassword.setText(fieldNewPasswordVisible.getText());
            fieldPassword.setVisible(true);
            fieldNewPasswordVisible.setVisible(false);

            fieldConfirmPassword.setText(fieldConfirmationVisible.getText());
            fieldConfirmPassword.setVisible(true);
            fieldConfirmationVisible.setVisible(false);
        }
    }
}
