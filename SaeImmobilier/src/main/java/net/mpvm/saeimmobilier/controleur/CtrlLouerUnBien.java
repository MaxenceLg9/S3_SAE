package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.BienLouable;

public class CtrlLouerUnBien {

    public DatePicker dateDebut;
    public DatePicker dateFin;
    public TextField fieldMontantLoyer;
    public TextField fieldTotalCharges;
    public TextField fieldDepotGarantie;
    public CheckBox checkBoxArchive;
    public CheckBox checkBoxRenouvelable;

    public Label labelAdresse;
    public Label labelNumeroFiscal;
    public Label labelType;
    public Label labelSurface;
    public Label labelNbPieces;
    public Label labelDateAjout;
    public Label labelNomBienL;

    public void initialize(){
        Platform.runLater(() -> {
            Stage stage = (Stage) labelAdresse.getScene().getWindow();
            BienLouable bienLouable = (BienLouable) stage.getProperties().get("bienLouable");
            if(bienLouable == null) {
                stage.close();
                return;
            }
            labelNomBienL.setText("Nom " + bienLouable.getIdProprio());
            labelAdresse.setText(bienLouable.getComplementAdresse() + ", " + bienLouable.getAdresse() + ", " + bienLouable.getCodePostal() + ", " + bienLouable.getVille());
            labelNumeroFiscal.setText("N° fiscal " + bienLouable.getNumeroFiscal());
            labelType.setText("Type " + bienLouable.getTypeBien().name());
            labelSurface.setText("Surface " + bienLouable.getSurface());
            labelNbPieces.setText(bienLouable.getNbPieces() + " pièces");
            labelDateAjout.setText("Ajouté le " + bienLouable.getDateAjout().toString());
        });
    }

}
