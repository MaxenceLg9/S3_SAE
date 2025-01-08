package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.controleur.CtrlViewBiensLouables;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueBiensLouables {

    private final int idImmeuble;

    public VueBiensLouables(int idImmeuble) {
        this.idImmeuble = idImmeuble;
    }

    public void startForImmeuble(Stage stage) {
        stage.getProperties().put("bien", this.idImmeuble);
        JfxUtil.updateStage(stage, "viewbienslouables.fxml", "Biens Louables de l'Immeuble", 750, 1200);
    }
}
