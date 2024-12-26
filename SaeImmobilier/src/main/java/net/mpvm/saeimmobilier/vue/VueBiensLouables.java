package net.mpvm.saeimmobilier.vue;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueBiensLouables {

    private int idImmeuble;

    public void startForImmeuble(Stage stage,int idImmeuble) {
        this.idImmeuble = idImmeuble;
        JfxUtil.updateStage(stage, "viewbienslouables.fxml", "Biens Louables de l'Immeuble " + idImmeuble,750, 800);
    }

    public static void showWindow(Stage stage, int idImmeuble) throws Exception {
        VueBiensLouables vue = new VueBiensLouables();
        vue.startForImmeuble(stage,idImmeuble);
    }
}
