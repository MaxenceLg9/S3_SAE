package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewLogement extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        JfxUtil.applicationInit(stage, "newlogement.fxml", "Ajouter un logement");
    }
}
