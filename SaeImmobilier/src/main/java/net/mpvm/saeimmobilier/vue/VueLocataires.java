package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueLocataires  extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        JfxUtil.updateStage(primaryStage, "viewlocataires.fxml", "Vue des Locataires", 750, 1200);
    }
}
