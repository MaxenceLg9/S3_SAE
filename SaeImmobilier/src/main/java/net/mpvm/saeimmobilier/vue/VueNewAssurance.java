package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueNewAssurance extends Application {

    @Override
    public void start(Stage stage) {
        JfxUtil.updateStage(stage, "newassurance.fxml","Création d'une Assurance",700,800);
    }
}
