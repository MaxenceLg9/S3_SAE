package net.mpvm.saeimmobilier.vue;
import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueNewTravaux extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JfxUtil.updateStage(stage, "newtravaux.fxml", "Ajouter des Travaux", 750, 850);
    }
}
