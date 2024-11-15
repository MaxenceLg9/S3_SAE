package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/net/mpvm/saeimmobilier/data/fxml/connexion.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        stage.setTitle("Connexion");
        Image icon = new Image(getClass().getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("esc"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}