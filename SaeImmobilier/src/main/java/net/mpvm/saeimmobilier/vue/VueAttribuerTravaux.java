package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerTravaux extends Application {

    public void start(Stage stage) {
        JfxUtil.updateStage(stage, "attribuerTravaux.fxml", "Attribuer des Travaux", 750, 1200);
    }
}
