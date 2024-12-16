package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewBien extends Application {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 750;

    @Override
    public void start(Stage stage) throws Exception {

        JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien", DEFAULT_HEIGHT, DEFAULT_WIDTH);
        stage.setResizable(false);
    }

    public static void showWindow(Stage stage) throws Exception {
        VueNewBien vue = new VueNewBien();
        vue.start(stage);
    }

}

