package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.mpvm.saeimmobilier.modele.Locataire;

public class CtrlNewLocataire {

    @FXML
    private TextField fieldPrenom;
    @FXML
    private TextField fieldNom;
    @FXML
    private TextField fieldEmail;

    @FXML
    public void initialize(){
        fieldNom.setPromptText("Nom du locataire");
        fieldPrenom.setPromptText("Prénom du locataire");
    }

    @FXML
    public void ajouterLocataire(){
        new Locataire(fieldNom.getText(), fieldPrenom.getText(), fieldEmail.getText()).save();
    }
}
