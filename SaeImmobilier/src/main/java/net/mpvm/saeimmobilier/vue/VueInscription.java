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
        JfxUtil.applicationInit(primaryStage, "inscription.fxml","Page d'authentification'",80, 800);
    }

    public static void showWindow(Stage stage) throws Exception {
        VueInscription vue = new VueInscription();
        vue.start(stage);
    }
}
