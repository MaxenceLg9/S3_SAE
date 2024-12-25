package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.controleur.CtrlImmeuble;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueImmeuble extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JfxUtil.applicationInit(primaryStage, "immeuble.fxml","Immeuble",700, 800);
    }
}