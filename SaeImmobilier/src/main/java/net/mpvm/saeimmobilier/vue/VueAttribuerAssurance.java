package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance  {

    private int bienId;

    public void startforBien(Stage stage, int bienId) {

        JfxUtil.updateStage(stage, "attribuerassurance.fxml","Attribuer une Assurance"+ bienId,700,800);
    }

}
