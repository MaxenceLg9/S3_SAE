package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.mpvm.saeimmobilier.modele.Locataire;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewLocataire {

    @FXML
    private AnchorPane anchorPaneRacine;
    @FXML
    private RadioButton radioButtonF;
    @FXML
    private RadioButton radioButtonM;
    @FXML
    private TextField fieldPrenom;
    @FXML
    private TextField fieldNom;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldTelephone;

    private List<TextField> fieldsLocataires;

    @FXML
    public void initialize(){
        fieldSetup();
        groupButton();
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsLocataires = new ArrayList<>(){
            {
                add(fieldNom);
                add(fieldPrenom);
                add(fieldEmail);
                add(fieldTelephone);
            }
        };
    }

    private void setFieldsPromptText() {
        fieldNom.setPromptText("Nom du locataire");
        fieldPrenom.setPromptText("Prénom du locataire");
        fieldEmail.setPromptText("Email du locataire");
        fieldTelephone.setPromptText("Téléphone du locataire");
    }

    private void groupButton() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonM.setSelected(true);
        radioButtonF.setToggleGroup(toggleGroup);
        radioButtonM.setToggleGroup(toggleGroup);
    }

    @FXML
    public void ajouterLocataire(){
        if(fieldsNotEmpty()) {
            char sexe = radioButtonF.isSelected() ? 'F' : 'M';
            new Locataire(fieldNom.getText(), fieldPrenom.getText(), fieldEmail.getText(), sexe, this.fieldTelephone.getText()).save();
        }
        else{
            alertFieldsEmpty();
        }
    }

    private void alertFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs");
        alert.showAndWait();
    }

    private boolean fieldsNotEmpty() {
        for(TextField textField : fieldsLocataires){
            if(textField.getText().isEmpty()){
                return false;
            }
        }
        return true;
    }

    public void annuler(ActionEvent actionEvent) {
        System.out.println("World Hello!");
    }
}
