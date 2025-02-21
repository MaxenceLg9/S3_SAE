package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueNewLocataire extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        JfxUtil.updateStage(primaryStage, "newlocataire.fxml", "Nouveau Locataire", 700, 800);
    }
}
