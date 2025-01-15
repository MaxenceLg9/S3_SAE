package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Garage;
import net.mpvm.saeimmobilier.modele.Habitation;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.sql.Date;
import java.time.LocalDate;

public class VueModifierBien extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        if(!stage.getProperties().containsKey("bien") || !(stage.getProperties().get("bien") instanceof Bien)) {
            stage.getProperties().put("bien", Garage.GARAGE);
//            stage.getProperties().put("bien", Immeuble.IMMEUBLE);
        }
        JfxUtil.updateStage(stage, "modifierBien.fxml", "Modifier un Bien", 750, 850);
    }
}