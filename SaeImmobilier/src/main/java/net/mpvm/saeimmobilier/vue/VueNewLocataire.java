package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewLocataire {
    private final int idBail;

    public VueNewLocataire(int idBail) {
        this.idBail = idBail;
    }

    public void startForLocataires(Stage stage) {
        stage.getProperties().put("bail", this.idBail);

        JfxUtil.updateStage(stage, "viewlocataires.fxml","Vision des locataires",750, 800);
    }
}
