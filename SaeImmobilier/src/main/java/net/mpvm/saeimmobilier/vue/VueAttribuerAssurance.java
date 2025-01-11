package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance extends Application {

    public void start(Stage stage) {
        JfxUtil.updateStage(stage, "attribuerAssurance.fxml", "Attribuer une Assurance", 750, 1200);
    }
}
