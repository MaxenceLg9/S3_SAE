package net.mpvm.saeimmobilier.vue;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import static javafx.application.Application.launch;

public class VueBails {
    private int IdBien;

    public void startForImmeuble(Stage stage,int IdBien) {
        this.IdBien = IdBien;
        JfxUtil.updateStage(stage, "viewBails.fxml", "Locations du Bien " + IdBien,700,800);
    }

    public void showWindow(Stage stage, int IdBien) {
        VueBails vue = new VueBails();
        vue.startForImmeuble(stage,IdBien);
    }
}
