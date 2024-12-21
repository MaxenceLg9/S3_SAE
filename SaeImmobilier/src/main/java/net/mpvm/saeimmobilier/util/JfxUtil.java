package net.mpvm.saeimmobilier.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class JfxUtil {
    public static void applicationInit(Stage primaryStage, String fxmlFile, String nomPage, double height, double width) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/" + fxmlFile));

            Scene scene = new Scene(fxmlLoader.load(), height, width);
            scene.getStylesheets().add(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/css/style.css").toExternalForm());

            Image icon = new Image(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString());

            primaryStage.setMinHeight(550);
            primaryStage.setMinWidth(700);
            primaryStage.setTitle(nomPage);
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayError(SQLException sqlException, String message) {
        setAlert(Alert.AlertType.ERROR, "Erreur", message, sqlException.getMessage());
    }

    public static void setAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        new Thread(() -> {Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        }).start();
    }
}
