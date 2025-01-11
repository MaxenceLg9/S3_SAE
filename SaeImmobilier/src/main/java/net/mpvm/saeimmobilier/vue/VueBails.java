package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import static javafx.application.Application.launch;

public class VueBails extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        JfxUtil.updateStage(primaryStage, "viewBails.fxml", "Locations du Bien ",700,800);
    }
}
