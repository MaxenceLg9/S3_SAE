module net.mpvm.saeimmobilier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires mysql.connector.j;
    requires org.slf4j;
    requires annotations;
    requires java.management;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires com.fasterxml.jackson.annotation;
    requires io;

    //opens net.mpvm.saeimmobilier.data.fxml to javafx.fxml;
    //opens net.mpvm.saeimmobilier.data.images to javafx.fxml;
    //opens net.mpvm.saeimmobilier.vue to javafx.fxml;
    //opens net.mpvm.saeimmobilier.controleur to javafx.fxml;
    exports net.mpvm.saeimmobilier.vue;
    exports net.mpvm.saeimmobilier.controleur;
    opens net.mpvm.saeimmobilier.controleur to javafx.fxml;
}
