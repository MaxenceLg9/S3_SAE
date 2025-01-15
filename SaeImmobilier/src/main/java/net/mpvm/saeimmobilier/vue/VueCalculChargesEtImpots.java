package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;

public class VueCalculChargesEtImpots extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        JfxUtil.updateStage(primaryStage, "calculs.fxml", "Calculs Impôts et Charges", 800, 800);
    }
}
