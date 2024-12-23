package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance extends Application {
    @Override
    public void start(Stage stage) {
        JfxUtil.applicationInit(stage, "attribuerassurance.fxml","Attribuer une Assurance",700,800);
    }
}
