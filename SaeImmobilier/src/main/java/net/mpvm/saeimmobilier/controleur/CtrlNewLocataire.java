package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewLocataire {

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

    @FXML
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
    public void ajouterLocataire(ActionEvent event) {
        if(fieldsNotEmpty()) {
            char sexe = radioButtonF.isSelected() ? 'F' : 'M';
            new Locataire(fieldNom.getText(), fieldPrenom.getText(), fieldEmail.getText(), sexe, this.fieldTelephone.getText()).save();
        }
        else{
            alertFieldsEmpty();
        }
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");

            Stage stage2 = (Stage) ((Button) event.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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

        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");

            Stage stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
