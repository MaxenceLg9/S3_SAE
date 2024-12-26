package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewBien extends Application {

    private static final int DEFAULT_WIDTH = 850;
    private static final int DEFAULT_HEIGHT = 750;

    @Override
    public void start(Stage stage) throws Exception {

        JfxUtil.updateStage(stage, "newbien.fxml", "Ajouter un Bien", DEFAULT_HEIGHT, DEFAULT_WIDTH);
        stage.setMinWidth(DEFAULT_WIDTH);
        stage.setMinHeight(DEFAULT_HEIGHT);
    }
}

