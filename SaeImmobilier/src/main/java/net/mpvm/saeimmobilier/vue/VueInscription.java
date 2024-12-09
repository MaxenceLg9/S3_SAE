package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;




public class VueInscription extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JfxUtil.applicationInit(primaryStage, "inscription.fxml","Page d'authentification'");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(800);
        primaryStage.setHeight(800);
        primaryStage.setWidth(800);
        primaryStage.setResizable(false);

    }
}
