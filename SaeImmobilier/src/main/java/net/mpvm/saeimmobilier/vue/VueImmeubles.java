package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueImmeubles extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Utilisation de JfxUtil pour initialiser la fenêtre
        JfxUtil.applicationInit(primaryStage, "viewimmeubles.fxml", "Liste des Immeubles");
    }
}
