module edu.iuh.fit.coffee.quanlycuahangcaphe {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires animatefx;

    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app to javafx.fxml;
    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app;

    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller;
    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller to javafx.fxml;
    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog;
    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog to javafx.fxml;
}