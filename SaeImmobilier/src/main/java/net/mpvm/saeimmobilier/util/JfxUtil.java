package net.mpvm.saeimmobilier.util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class JfxUtil {

    public static void updateStage(Stage primaryStage, String fxmlFile, String nomPage){
        updateStage(primaryStage, fxmlFile, nomPage, 0, 0);
    }

    public static void updateStage(Stage primaryStage, String fxmlFile, String nomPage, double height, double width) {
        try {
            setScene(primaryStage,fxmlFile);
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
            System.out.println("Erreur lors de l'affichage de l'application");
        }
    }



    private static void setScene(Stage primaryStage, String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/fxml/"+fxml));
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

    public static Float doubleToFloat(double d){

        return ((Double) d).floatValue();
    }
}