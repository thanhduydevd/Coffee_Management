module com.damcafe.app {
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires animatefx;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;

    exports com.damcafe.app.controller;
    opens com.damcafe.app.controller to javafx.fxml;

    exports com.damcafe.app.controller.dialog;
    opens com.damcafe.app.controller.dialog to javafx.fxml;

    exports com.damcafe.app.gui;
    opens com.damcafe.app.gui to javafx.fxml;
    exports com.damcafe.app.entity;
}