package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAccueil extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JfxUtil.updateStage(primaryStage, "accueil.fxml","HomePage",800, 700);
    }
}