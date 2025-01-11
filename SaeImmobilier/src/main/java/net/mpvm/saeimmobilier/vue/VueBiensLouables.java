package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueBiensLouables extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        JfxUtil.updateStage(primaryStage, "viewBiensLouables.fxml", "Biens Louables de l'Immeuble", 750, 1200);
    }
}
