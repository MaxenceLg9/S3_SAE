package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueMdpOublie extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JfxUtil.applicationInit(primaryStage, "mdpoublie.fxml","Modification mot de passe",750, 800);
        primaryStage.setResizable(false);
    }

    public static void showWindow(Stage stage) throws Exception {
        VueMdpOublie vue = new VueMdpOublie();
        vue.start(stage);
    }
}
