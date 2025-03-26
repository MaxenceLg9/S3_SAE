package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.vue.VueConnexion;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;


public class CtrlMdpOublie {

    @FXML
    Button btnModifier;
    @FXML
    Button BtnQuitter;
    @FXML
    TextField FieldEmail;
    @FXML
    TextField FieldNewPwd1;
    @FXML
    TextField FieldNewPwd2;


    public void Quitter(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueConnexion.class);
    }

    public void Modifier(javafx.event.ActionEvent actionEvent) throws Queryable.QbleException {
        String email = this.FieldEmail.getText().trim();
        String newPassword = this.FieldNewPwd1.getText();
        String confirmPassword = this.FieldNewPwd2.getText();

        if (email.isEmpty()) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Email manquant", "Veuillez entrer votre adresse email");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Mots de passe différents !", "Veuillez entrer les mêmes mot de passe");
            return;
        }

        try {
            Proprietaire proprietaire = new Proprietaire(email, newPassword);
            proprietaire.modify();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe modifié", "Votre mot de passe a été modifié avec succès");
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            JfxUtil.showWindow(stage, VueConnexion.class);
        } catch (Proprietaire.ProprietaireException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de modification", e.getMessage());
        }
    }

}