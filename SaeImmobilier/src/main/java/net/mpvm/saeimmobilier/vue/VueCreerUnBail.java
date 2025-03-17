package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Garage;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.util.JfxUtil;
/**
 * Configure la scène principale avec le fichier FXML spécifié et les propriétés de la fenêtre.
 */
public class VueCreerUnBail extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!primaryStage.getProperties().containsKey("idBien")){
            System.out.println("Starting transaction at" + this.getClass().getSimpleName());
            QueryElement.newStaticConnection();
            primaryStage.getProperties().put("bienLouable", Garage.GARAGE);
            Garage.GARAGE.save();
            Garage.GARAGE.getImmeuble().save();
            primaryStage.setOnCloseRequest(e -> {
                QueryElement.rollBackStaticConnection();
                QueryElement.removeStaticConnection();
            });
        } else {
            // Si idBien est passé, on initialise la connexion
            QueryElement.newStaticConnection();
            primaryStage.setOnCloseRequest(e -> {
                QueryElement.rollBackStaticConnection();
                QueryElement.removeStaticConnection();
            });
        }
        JfxUtil.updateStage(primaryStage, "creerUnBail.fxml", "Louer un bien");
    }
}