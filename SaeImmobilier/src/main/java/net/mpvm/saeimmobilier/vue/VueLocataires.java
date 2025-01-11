package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueLocataires  {
    private final int idBail;

    public VueLocataires(int idBail) {
        this.idBail = idBail;
    }


    public void startForBail(Stage stage) {
        stage.getProperties().put("bail", this.idBail);

        JfxUtil.updateStage(stage, "viewlocataires.fxml","Vision des locataires",750, 800);
    }
}
