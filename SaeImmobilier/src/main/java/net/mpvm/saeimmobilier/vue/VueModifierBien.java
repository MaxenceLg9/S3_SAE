package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueModifierBien extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        JfxUtil.updateStage(stage, "modifierBien.fxml", "Modifier un Bien", 750, 850);
    }
}
