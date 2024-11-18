package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JfxUtil.applicationInit(stage, "connexion.fxml", "First page");
    }

    public static void main(String[] args) {
        launch(args);
    }
}