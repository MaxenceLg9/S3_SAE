package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Garage;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueLouerUnBien extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!primaryStage.getProperties().containsKey("bienLouable")){
            QueryElement.newStaticConnection();
            primaryStage.getProperties().put("bienLouable", Garage.GARAGE);
            Garage.GARAGE.save();
            Garage.GARAGE.getImmeuble().save();
            primaryStage.setOnCloseRequest(_ -> {
                QueryElement.rollBackStaticConnection();
                QueryElement.removeStaticConnection();
            });
        }
        JfxUtil.updateStage(primaryStage, "louerunbien.fxml", "Louer un bien");
    }
}