package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Garage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.sql.Date;
import java.time.LocalDate;

public class VueLouerUnBien extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!primaryStage.getProperties().containsKey("bienLouable")){
            primaryStage.getProperties().put("bienLouable", Garage.GARAGE);
        }
        JfxUtil.updateStage(primaryStage, "louerunbien.fxml", "Louer un bien");
    }
}