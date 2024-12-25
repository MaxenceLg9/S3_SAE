package net.mpvm.saeimmobilier.util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.Size;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.controleur.CtrlAccueil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class JfxUtil {
    public static void applicationInit(Stage primaryStage, String fxmlFile, String nomPage, double height, double width) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/" + fxmlFile));

            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/css/style.css").toExternalForm());
            System.out.println(scene.getRoot().getLayoutBounds().getWidth());

            Image icon = new Image(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png").toString());

            primaryStage.setTitle(nomPage);
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
            // Get the scene's actual layout dimensions
            Platform.runLater(() -> {
                primaryStage.setMinHeight(0);
                primaryStage.setMinWidth(0);
                primaryStage.sizeToScene();
                primaryStage.setMinHeight(primaryStage.getHeight());
                primaryStage.setMinWidth(primaryStage.getWidth());
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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