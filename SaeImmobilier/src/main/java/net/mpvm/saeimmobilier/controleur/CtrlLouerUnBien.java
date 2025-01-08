package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.BienLouable;

public class CtrlLouerUnBien {

    @FXML
    public Label fieldAdresseBienL;
    @FXML
    private Label fieldNomBienL;

    public void initialize(){
        Platform.runLater(() -> {
            Stage stage = (Stage) fieldAdresseBienL.getScene().getWindow();
            BienLouable bienLouable = (BienLouable) stage.getProperties().get("bienLouable");
            if(bienLouable == null) {
                stage.close();
                return;
            }
            fieldNomBienL.setText(bienLouable.getIdProprio());
            fieldAdresseBienL.setText(bienLouable.getComplementAdresse() + " " + bienLouable.getAdresse() + "," + bienLouable.getVille());
        });
    }

}
