module net.mpvm.saeimmobilier.saeimmobilier {
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
    requires java.desktop;

    opens net.mpvm.saeimmobilier.saeimmobilier to javafx.fxml;
    exports net.mpvm.saeimmobilier.saeimmobilier;
}