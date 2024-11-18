package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;

public class VueNewLocataire extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        JfxUtil.applicationInit(primaryStage, "newlocataire.fxml", "Ajouter un locataire");
    }


}
