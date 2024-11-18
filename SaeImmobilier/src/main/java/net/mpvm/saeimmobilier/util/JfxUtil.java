package net.mpvm.saeimmobilier.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.vue.HelloApplication;

import java.io.IOException;

public class JfxUtil {
    public static void applicationInit(Stage primaryStage, String fxmlFile, String nomPage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/" + fxmlFile));

        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        scene.getStylesheets().add(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/css/style.css").toExternalForm());

        Image icon = new Image(HelloApplication.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString());

        primaryStage.setTitle(nomPage);
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
