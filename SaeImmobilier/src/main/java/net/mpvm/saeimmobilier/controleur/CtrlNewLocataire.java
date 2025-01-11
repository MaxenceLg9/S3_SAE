package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;

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
    private int idBail;

    @FXML
    public void initialize(){
        fieldSetup();
        groupButton();
        fieldNom.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBail(stage);
                } else {
                    System.out.println("pas de stage");
                }
            } else {
                System.out.println("pas de scène");
            }
        });
    }
    public void setIdBail(Stage stage) {
        Object id = stage.getProperties().get("bail");
        if (id instanceof Integer) {
            this.idBail = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bail' manquante ou incorrecte.");
        }
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
        fieldTelephone.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 10 && change.getControlNewText().matches("\\d*") ? change : null
        ));
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
            try {
                new Locataire(fieldNom.getText(), fieldPrenom.getText(), fieldEmail.getText(), sexe, this.fieldTelephone.getText()).save();
            } catch (Locataire.LocataireException e) {
                e.getSqlException().printStackTrace();
            }
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

    public void annuler(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueAccueil.class);
    }

    public void Accueil(ActionEvent event ) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        JfxUtil.showWindow(stage, VueAccueil.class);
    }
}