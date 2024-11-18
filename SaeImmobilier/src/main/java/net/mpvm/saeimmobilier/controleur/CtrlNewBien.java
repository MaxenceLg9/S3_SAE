package net.mpvm.saeimmobilier.controleur;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import net.mpvm.saeimmobilier.modele.Immeuble;

public class CtrlNewBien {

    @FXML
    private ChoiceBox<Immeuble> listImmeubles;

    @FXML
    public void initialize() {
        // Initialize the list of Immeubles
        if (listImmeubles != null) {
            listImmeubles.getItems().add(new Immeuble(1, 1, "Rue du U", "Résidence du TDC"));
            System.out.println(listImmeubles.getItems().getFirst().getAdresse());
        } else {
            System.out.println("ChoiceBox listImmeubles is not injected");
        }
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
