package net.mpvm.saeimmobilier.util;

import com.mysql.cj.xdevapi.Table;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JfxUtil {

    public static final String BUTTON_VALIDER = "button-valider";
    public static final String BUTTON_SUPPRIMER = "button-supprimer";
    public static final String ASSURANCE_TITLE = "assurance-title";
    public static final String ASSURANCE_GRIDPANE = "assurance-gridpane";
    public static final String ASSURANCE_LABEL = "assurance-label";
    public static final String COL_VILLE = "col-ville";
    public static final String STYLE_CELL = "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-color: #1e2d3e;";
    public static final String STYLE_CELL_HOVER = "-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: white;";
    public static final String STYLE_BORDER_CELL = "-fx-border-color: black; -fx-border-width: 0 0 1 0;";

    public static void updateStage(Stage primaryStage, String fxmlFile, String nomPage){
        updateStage(primaryStage, fxmlFile, nomPage, 0, 0);
    }

    public static void updateStage(Stage primaryStage, String fxmlFile, String nomPage, double height, double width) {
        try {
            setScene(primaryStage,fxmlFile);
            primaryStage.setTitle(nomPage);
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

    public static Optional<ButtonType> setAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public static void showWindow(Stage stage, Class<? extends Application> applicationClass) {
        try {
            ((Application) applicationClass.getConstructors()[0].newInstance()).start(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int askForDelete(String message) {
        Optional<ButtonType> result = JfxUtil.setAlert(Alert.AlertType.CONFIRMATION, "Confirmation de la suppression", message, "Cette action est irréversible");
        return result.isPresent() && result.get().equals(ButtonType.OK) ? 1 : 0;
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
        scene.getStylesheets().add(Objects.requireNonNull(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/css/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(JfxUtil.class.getResource("/net/mpvm/saeimmobilier/data/images/icon_immobilier.png")).toString()));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static Float doubleToFloat(Object d){

        return ((Double) d).floatValue();
    }

    public static void setClass(String className, Node... nodes){
        for (Node node : nodes) node.getStyleClass().add(className);

    }

    public static void updateRow(Queryable item, boolean empty, List<TableRow<?>> register, TableRow<?> tableRow) {
        if (empty || item == null) {
            tableRow.setStyle(""); // Reset style for empty rows
        } else {
            if (tableRow.isSelected()) {
                tableRow.setStyle(STYLE_BORDER_CELL+"-fx-background-color: #336699; -fx-text-fill: white;"); // Apply hover style
            } else {
                tableRow.setStyle(STYLE_BORDER_CELL); // Reset style for unselected rows
            }

            tableRow.setOnMouseEntered(event -> {
                if (!tableRow.isSelected()) {
                    for (int i = 0; i < tableRow.getChildrenUnmodifiable().size(); i++) {
                        if (tableRow.getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                            cell.setStyle(STYLE_CELL_HOVER);
                        }
                    }
                }
            });
            tableRow.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1){
                    for(TableRow<?> row : register){
                        for (int i = 0; i < row.getChildrenUnmodifiable().size(); i++) {
                            if (row.getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                                cell.setStyle(STYLE_CELL);
                            }
                        }
                    }
                    for (int i = 0; i < tableRow.getChildrenUnmodifiable().size(); i++) {
                        if (tableRow.getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                            cell.setStyle(STYLE_CELL_HOVER);
                        }
                    }
                }
            });
            tableRow.setOnMouseExited(event -> {
                if (!tableRow.isSelected()) {
                    for (int i = 0; i < tableRow.getChildrenUnmodifiable().size(); i++) {
                        if (tableRow.getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                            cell.setStyle(STYLE_CELL);
                        }
                    }
                }
            });
        }
    }
}