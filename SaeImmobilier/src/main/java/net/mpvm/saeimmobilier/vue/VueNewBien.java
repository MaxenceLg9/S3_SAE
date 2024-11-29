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
        stage.setMinWidth(1300);
        stage.setMinHeight(900);
        stage.setHeight(900);
        stage.setWidth(1300);
    }

    public void show() {
        try {
            // Charger le fichier FXML associé à la vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newbien.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec des dimensions explicites
            Scene scene = new Scene(root, 1300, 900);

            // Créer et configurer le stage
            Stage stage = new Stage();
            stage.setMinWidth(1300);
            stage.setMinHeight(900);

            stage.setTitle("Ajouter un Bien");
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

