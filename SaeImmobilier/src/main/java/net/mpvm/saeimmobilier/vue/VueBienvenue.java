package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;


public class VueBienvenue extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        JfxUtil.updateStage(primaryStage, "bienvenue.fxml", "Accueil", 800, 800);
    }
}