package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.util.JfxUtil;

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

    public void Quitter(javafx.event.ActionEvent actionEvent) {
        Stage stage1 = new Stage();
        JfxUtil.applicationInit(stage1, "accueil.fxml", "Accueil");
        stage1.show();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void mdpOublie(javafx.event.ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            JfxUtil.applicationInit(stage, "mdpoublie.fxml", "Modifier son mot de passe");
            Stage stageActu = (Stage) ((Hyperlink) actionEvent.getSource()).getScene().getWindow();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connexion(ActionEvent actionEvent) {
        if (isMailNull()){
            alertMailEmpty();
        }else if(isPwdNull()){
            alertPwdEmpty();
        } else if (isValidEmail(this.FieldMail.getText())){
                try {
                    Map<String,Proprietaire> proprietaires = Proprietaire.findALl().stream().filter(proprietaire -> proprietaire.getEmail().equals(this.FieldMail.getText())).collect(Collectors.toMap(Proprietaire::getMotDePasse, Function.identity()));
                    for(Proprietaire p : proprietaires.values()) {
                        System.out.println(p.getMotDePasse());
                        System.out.println(this.FieldPwd.getText() + " le label");
                        if (this.FieldPwd.getText().equals(p.getMotDePasse())){
                            Stage stage = new Stage();
                            JfxUtil.applicationInit(stage, "newbien.fxml", "Création d'un bien");

                            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            stageActu.close();

                            stage.show();
                        }
                        alertIncorrectEmpty();
                    }
                } catch (Proprietaire.ProprietaireException e) {
                    e.printStackTrace();
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