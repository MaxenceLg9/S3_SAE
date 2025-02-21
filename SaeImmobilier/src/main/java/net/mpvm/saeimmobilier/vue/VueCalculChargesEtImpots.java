package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueCalculChargesEtImpots extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        JfxUtil.updateStage(primaryStage, "calculs.fxml", "Calculs Impôts et Charges", 800, 800);
    }
}
