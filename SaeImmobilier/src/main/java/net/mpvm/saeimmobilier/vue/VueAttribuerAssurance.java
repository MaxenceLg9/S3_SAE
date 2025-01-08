package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance extends Application {

    public void start(Stage stage) {
        JfxUtil.updateStage(stage, "attribuerassurance.fxml","Attribuer une Assurance",700,800);
    }

    public static void main(String[] args) {
        launch(args);
    }
}