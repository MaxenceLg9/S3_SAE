package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.io.IOException;


public class VueAccueil extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setScene(primaryStage);
        JfxUtil.resize(primaryStage);
    }

    private void setScene(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/accueil.fxml"));
        if(primaryStage.getScene() == null) {
            instantiateStage(primaryStage, fxmlLoader);
        }
        else {
            primaryStage.getScene().setRoot(fxmlLoader.load());
        }
    }

    private static void instantiateStage(Stage primaryStage, FXMLLoader fxmlLoader) throws IOException {
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString()));
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}