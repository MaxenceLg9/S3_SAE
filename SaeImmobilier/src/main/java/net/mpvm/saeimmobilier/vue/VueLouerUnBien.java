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
            Immeuble i = new Immeuble.IBuilder("Quimpermeable", "2", "Rue de la goutte", "2", Date.valueOf(LocalDate.now()), "BatimentJSP").build();
            primaryStage.getProperties().put("bienLouable", new Garage.GBuilder("RATATA", 1, "1", i, 1, "Appartement du Batiment", Date.valueOf(LocalDate.now())).build());
        }
        JfxUtil.updateStage(primaryStage, "louerunbien.fxml", "Louer un bien");
    }
}