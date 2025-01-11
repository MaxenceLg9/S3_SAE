package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewLocataire extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        JfxUtil.updateStage(primaryStage, "newlocataire.fxml", "Ajout d'un Locataire", 750, 1200);
    }
}
