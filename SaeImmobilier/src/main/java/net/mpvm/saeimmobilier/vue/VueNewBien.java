package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueNewBien extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        JfxUtil.applicationInit(stage, "newbien.fxml", "Ajouter un Bien");
    }

    public void show() {
        try {
            // Charger le fichier FXML associé à la vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VueNewBien.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et un stage
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Bien");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
