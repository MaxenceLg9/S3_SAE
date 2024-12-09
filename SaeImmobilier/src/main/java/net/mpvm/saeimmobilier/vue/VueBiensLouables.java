package net.mpvm.saeimmobilier.vue;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueBiensLouables {

    private int idImmeuble;

    public void startForImmeuble(int idImmeuble) {
        this.idImmeuble = idImmeuble;
        Stage stage = new Stage();
        JfxUtil.applicationInit(stage, "viewbienslouables.fxml", "Biens Louables de l'Immeuble " + idImmeuble);
    }
}
