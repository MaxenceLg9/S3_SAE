package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueHome extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JfxUtil.applicationInit(primaryStage, "home.fxml","HomePage",800, 700);
        primaryStage.setResizable(false);
    }

    public static void showWindow(Stage stage) throws Exception {
        VueHome vue = new VueHome();
        vue.start(stage);
    }
}