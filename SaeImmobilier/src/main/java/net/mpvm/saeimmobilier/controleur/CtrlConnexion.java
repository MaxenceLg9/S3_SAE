package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;

import javafx.scene.input.KeyEvent;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.*;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CtrlConnexion {
    @FXML
    public TextField FieldMail;
    @FXML
    public PasswordField FieldPwd;
    @FXML
    private Label welcomeText;
    @FXML
    private Button BtwQuitter;

    public void initialize(){
        try{
            if (Proprietaire.findAll().isEmpty()){
                JfxUtil.setAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Il n'existe pas de propriétaire propriétaire",
                        "Essayez de vous inscrire");
                Platform.runLater(() -> JfxUtil.showWindow(((Stage) welcomeText.getScene().getWindow()), VueInscription.class));
            }
        } catch (Proprietaire.ProprietaireException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Erreur lors de la récupération des données",
                    "Vérifier votre connexion");
        }
    }

    public void Quitter(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueBienvenue.class);
    }

    public void mdpOublie(ActionEvent actionEvent) {
        // Créer une nouvelle fenêtre (Stage)
        Stage stage = (Stage) this.welcomeText.getScene().getWindow();
        JfxUtil.showWindow(stage, VueMdpOublie.class);
    }

    @FXML
    public void ConnexionEnter(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            if (isMailNull()){
                alertMailEmpty();
            }else if(isPwdNull()){
                alertPwdEmpty();
            } else if (isValidEmail(this.FieldMail.getText())){
                try {
                    Map<String,Proprietaire> proprietaires = Proprietaire.findAll().stream().filter(p-> p.getEmail().equals(this.FieldMail.getText())).collect(Collectors.toMap(Proprietaire::getPassword, Function.identity()));
                    for(Proprietaire p : proprietaires.values()) {
                        if (this.FieldMail.getText().equals(p.getEmail())) {
                            if (this.FieldPwd.getText().equals(p.getPassword())) {

                                Stage stage = (Stage) ((PasswordField) event.getSource()).getScene().getWindow();
                                JfxUtil.showWindow(stage, VueAccueil.class);
                            }else {
                                alertIncorrectEmpty();
                            }
                        }else {
                            alertIncorrectEmpty();
                        }
                    }
                } catch (Proprietaire.ProprietaireException e) {
                    alertIncorrectEmpty();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {alertFormatMail();
            }
        }
    }

    @FXML
    public void Connexion(ActionEvent actionEvent) {
        if (isMailNull()){
            alertMailEmpty();
        }else if(isPwdNull()){
            alertPwdEmpty();
        } else if (isValidEmail(this.FieldMail.getText())){
            try {
                Map<String,Proprietaire> proprietaires = Proprietaire.findAll().stream().filter(Proprietaire-> Proprietaire.getEmail().equals(this.FieldMail.getText())).collect(Collectors.toMap(Proprietaire::getPassword, Function.identity()));
                for(Proprietaire p : proprietaires.values()) {
                    System.out.println(this.FieldMail.getText());
                    System.out.println(p.getEmail());
                    if (this.FieldMail.getText().equals(p.getEmail())) {
                        if (this.FieldPwd.getText().equals(p.getPassword())) {
                            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            JfxUtil.showWindow(stage, VueAccueil.class);
                        }
                    }else {
                        alertIncorrectEmpty();
                    }
                }
            } catch (Proprietaire.ProprietaireException e) {
                alertIncorrectEmpty();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else {alertFormatMail();
        }

    }

    public boolean isMailNull(){
        return this.FieldMail.getText() == null;
    }

    public boolean isPwdNull(){
        return this.FieldPwd.getText()== null;
    }

    private void alertFormatMail() {
        JfxUtil.displayError("Adresse mail invalide","Vérifier le format du mail");
    }

    private void alertMailEmpty() {
        JfxUtil.displayError("Mail non précisé","Veuillez remplir tous les champs");
    }

    private void alertPwdEmpty() {
        JfxUtil.displayError("Mot passe non entré","Veuillez remplir tous les champs");
    }

    private void alertIncorrectEmpty() {
        JfxUtil.displayError("Identifiant ou mot de passe Incorrect","Veuillez vérifier votre mail et votre mot de passe");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-.]+@[\\w-.]+\\.\\w{2,}$";
        return Pattern.matches(emailRegex, email);
    }


}