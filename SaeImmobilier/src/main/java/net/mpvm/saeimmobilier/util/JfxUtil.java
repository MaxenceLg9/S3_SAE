package net.mpvm.saeimmobilier.util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class JfxUtil {
    public static void updateStage(Stage primaryStage, String fxmlFile, String nomPage, double height, double width) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/" + fxmlFile));

            primaryStage.getScene().setRoot(fxmlLoader.load());
            primaryStage.setTitle(nomPage);
            // Get the scene's actual layout dimensions
            resize(primaryStage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resize(Stage primaryStage) {
        Platform.runLater(() -> {
            if(!primaryStage.isMaximized()){
                primaryStage.setMinHeight(0);
                primaryStage.setMinWidth(0);
                primaryStage.sizeToScene();

                System.out.println(primaryStage.getScene().getWidth() + " * " + primaryStage.getScene().getHeight());
                System.out.println(primaryStage.getWidth() + " * " + primaryStage.getHeight());

                primaryStage.setMinHeight(primaryStage.getHeight());
                primaryStage.setMinWidth(primaryStage.getWidth());
            }
            else{
                System.out.println("Maximized");
                primaryStage.setMaximized(true);
            }
        });
    }


    public static void displayError(String erreur, String message) {
        setAlert(Alert.AlertType.ERROR, "Erreur", erreur, message);
    }

    public static void setAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void showWindow(Stage stage, Class<? extends Application> applicationClass) {
        try {
            ((Application) applicationClass.getConstructors()[0].newInstance()).start(stage);
        } catch (Exception e) {
            System.out.println("والآن أصبحت الموت، مدمر العالم");
        }
    }
}