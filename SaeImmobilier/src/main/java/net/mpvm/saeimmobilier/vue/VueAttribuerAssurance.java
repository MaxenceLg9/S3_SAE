package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.controleur.CtrlViewBiensLouables;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance {

    private final int idBien;

    public VueAttribuerAssurance(int idBien) {
        this.idBien = idBien;
    }

    public void startForImmeuble(Stage stage) {
        stage.getProperties().put("bien", this.idBien);
        JfxUtil.updateStage(stage, "attribuerAssurance.fxml", "Attribuer une Assurance", 750, 1200);
    }
}
