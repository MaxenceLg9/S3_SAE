package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.vue.VueConnexion;


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

    public void Modifier(javafx.event.ActionEvent actionEvent) {
        if(this.FieldNewPwd1.getText().length()>=5 && this.FieldNewPwd2.getText().length()>= 5) {
            if (this.FieldNewPwd1.getText().equals(this.FieldNewPwd2.getText())) {
                System.out.println(this.FieldNewPwd1.getText());
                System.out.println(0);
            } else {
                JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Mots de passe différents !", "Veuillez entrer les mêmes mot de passe");

            }
        }else {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Mot de passe trop petit !", "La taille du mot de passe doit être d'au moins 5 caractères ");
        }
    }

}