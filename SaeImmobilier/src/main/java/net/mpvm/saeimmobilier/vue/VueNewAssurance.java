package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewAssurance extends Application {

    @Override
    public void start(Stage stage) {
        JfxUtil.applicationInit(stage, "newassurance.fxml","Création d'une Assurance",700,800);
    }
    public static void showWindow(Stage stage) throws Exception {
        VueNewAssurance vue = new VueNewAssurance();
        vue.start(stage);
    }
}
