package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class VueNewBien extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/net/mpvm/saeimmobilier/data/fxml/newbien.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        scene.getStylesheets().add(getClass().getResource("/net/mpvm/saeimmobilier/data/css/style.css").toExternalForm());

        Image icon = new Image(HelloApplication.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString());

        stage.setTitle("Ajouter un Bien");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
