package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import net.mpvm.saeimmobilier.modele.Immeuble;

public class CtrlNewLogement {

    @FXML
    private ChoiceBox<Immeuble> listImmeubles;
    @FXML
    private TextField inputSurfaceHabitable;

    @FXML
    public void initialize() {
        // Initialize the list of Immeubles
        if (listImmeubles != null) {
            listImmeubles.getItems().add(new Immeuble(1, 1, "Rue du U", "Résidence du TDC"));
            System.out.println(listImmeubles.getItems().getFirst().getAdresse());
        } else {
            System.out.println("ChoiceBox listImmeubles is not injected");
        }
        inputSurfaceHabitable.setPromptText("Insérez la surface habitable en m²");
    }

    @FXML
    public void ajouterBien(ActionEvent actionEvent) {
        System.out.println("Hello World!");
    }

    @FXML
    public void supprimerBien(ActionEvent actionEvent) {
        System.out.println("World Hello!");
    }
}
