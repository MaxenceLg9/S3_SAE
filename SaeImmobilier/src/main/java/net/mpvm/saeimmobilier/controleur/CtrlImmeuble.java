package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;

public class CtrlImmeuble {

    @FXML
    private Label adresse;

    @FXML
    private Node rootNode;

    private Immeuble immeuble;

    public void initialize(){
        Stage s = (Stage) rootNode.getScene().getWindow();
        if(s.getProperties().containsKey("immeuble") && s.getProperties().get("immeuble") != null && s.getProperties().get("immeuble") instanceof Immeuble)
            setImmeuble(s);


    }

    private void setImmeuble(Stage s) {
        immeuble = (Immeuble) s.getProperties().get("immeuble");
        adresse.setText(immeuble.getAdresse());
    }
}
