package net.mpvm.saeimmobilier.vue;

import javafx.application.Application;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.util.JfxUtil;

public class VueAttribuerAssurance  {

    public void startforBien(Stage stage,int bienId) {
        JfxUtil.updateStage(stage, "attribuerassurance.fxml","Attribuer une Assurance",700,800);
    }
    public static void showWindow(Stage stage, int bienId) throws Exception {
        VueAttribuerAssurance vue = new VueAttribuerAssurance();
        vue.startforBien(stage, bienId);
    }
}
