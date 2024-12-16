package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.util.JfxUtil;

import javafx.scene.input.KeyEvent;
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

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void Quitter(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage1 = new Stage();
        VueAccueil.showWindow(stage1);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void mdpOublie(javafx.event.ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            VueMdpOublie.showWindow(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Connexion1(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            if (isMailNull()){
                alertMailEmpty();
            }else if(isPwdNull()){
                alertPwdEmpty();
            } else if (isValidEmail(this.FieldMail.getText())){
                try {
                    Map<String,Proprietaire> proprietaires = Proprietaire.findAll().stream().filter(Proprietaire-> Proprietaire.getEmail().equals(this.FieldMail.getText())).collect(Collectors.toMap(Proprietaire::getMotDePasse, Function.identity()));
                    for(Proprietaire p : proprietaires.values()) {
                        if (this.FieldMail.getText().equals(p.getEmail())) {
                            if (this.FieldPwd.getText().equals(p.getMotDePasse())) {
                                Stage stage = new Stage();
                                VueHome.showWindow(stage);
                                Stage stageActuel = (Stage) ((PasswordField) event.getSource()).getScene().getWindow();
                                stageActuel.close();
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
                Map<String,Proprietaire> proprietaires = Proprietaire.findAll().stream().filter(Proprietaire-> Proprietaire.getEmail().equals(this.FieldMail.getText())).collect(Collectors.toMap(Proprietaire::getMotDePasse, Function.identity()));
                for(Proprietaire p : proprietaires.values()) {
                    System.out.println(this.FieldMail.getText());
                    System.out.println(p.getEmail());
                    if (this.FieldMail.getText().equals(p.getEmail())) {
                        if (this.FieldPwd.getText().equals(p.getMotDePasse())) {
                            Stage stage = new Stage();
                            try {
                                VueHome.showWindow(stage);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            stageActu.close();

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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Adresse mail invalide");
        alert.setContentText("Vérifier le format du mail");
        alert.showAndWait();
    }

    private void alertMailEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Mail non précisé");
        alert.setContentText("Veuillez remplir tous les champs");
        alert.showAndWait();
    }

    private void alertPwdEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Mot passe non entré");
        alert.setContentText("Veuillez remplir tous les champs");
        alert.showAndWait();
    }

    private void alertIncorrectEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Identifiant ou mot de passe Incorrect");
        alert.setContentText("Veuillez vérifier votre mail et votre mot de passe");
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-.]+@[\\w-.]+\\.\\w{2,}$";
        return Pattern.matches(emailRegex, email);
    }


}