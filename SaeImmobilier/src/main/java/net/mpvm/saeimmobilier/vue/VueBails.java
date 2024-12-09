package net.mpvm.saeimmobilier.vue;

import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

import static javafx.application.Application.launch;

public class VueBails {
    private int IdBien;

    public void startForImmeuble(int IdBien) {
        this.IdBien = IdBien;
        Stage stage = new Stage();
        JfxUtil.applicationInit(stage, "viewBails.fxml", "Locations du Bien " + IdBien);
    }
}
