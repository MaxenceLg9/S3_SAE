package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewBien extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");
        stage.setMinWidth(800);
        stage.setMinHeight(750);
        stage.setHeight(750);
        stage.setWidth(800);
        stage.setResizable(false);
    }

}

