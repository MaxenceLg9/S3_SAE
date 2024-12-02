package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CtrlAuthentification {
    private static final Logger log = LoggerFactory.getLogger(CtrlAuthentification.class);
    @FXML
    public Button btnValider;

    @FXML
    public Button btnAnnuler;

    @FXML
    public TextField fieldConfirmation;

    @FXML
    public TextField fieldNewPassword;

    @FXML
    public CheckBox checkBoxVisibilite;

    @FXML
    public TextField fieldMail;

    private ArrayList<TextField> fieldsMDP;
    @FXML
    public void initialize() {
        fieldSetup();
        checkBoxVisibilite.setSelected(false);
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsMDP = new ArrayList<>(){
            {
                add(fieldMail);
                add(fieldNewPassword);
                add(fieldConfirmation);

            }
        };
    }

    private void setFieldsPromptText() {
        fieldMail.setText("Adresse mail Propriétaire");
        fieldNewPassword.setText("Mot de Passe Propriétaire");
        fieldConfirmation.setText("Confirmation Nouveau Mot de Passe");

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
        if(fieldsNotEmpty()){

        }
    }
    @FXML
    public void Annuler(ActionEvent event) {

    }
    @FXML
    public void Visible(ActionEvent event) {

    }
    private boolean fieldsNotEmpty() {
        for(TextField textField : fieldsMDP){
            if(textField.getText().isEmpty()){
                return false;
            }
        }
        return true;
    }
    public void ajouterLocataire(){
        if(fieldsNotEmpty()) {

            try {
                new Proprietaire(fieldMail.getText(), fieldNewPassword.getText()).save();
            } catch (Proprietaire.ProprietaireException e) {
                //TODO : handle exception
            }
        }
        else{
            alertFieldsEmpty();
        }
    }
}
