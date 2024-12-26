package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewLocataire extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        JfxUtil.updateStage(primaryStage, "newlocataire.fxml", "Ajouter un locataire",750, 800);
    }

    public static void showWindow(Stage stage) throws Exception {
        VueNewLocataire vue = new VueNewLocataire();
        vue.start(stage);
    }

}
