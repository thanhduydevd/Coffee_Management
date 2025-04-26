module edu.iuh.fit.coffee.quanlycuahangcaphe {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires animatefx;
<<<<<<< HEAD
    requires javafx.graphics;

    exports com.damcafe.app.controller;
    opens com.damcafe.app.controller to javafx.fxml;
    exports com.damcafe.app.controller.dialog;
    opens com.damcafe.app.controller.dialog to javafx.fxml;
    exports com.damcafe.app.gui;
    opens com.damcafe.app.gui to javafx.fxml;
=======

    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app to javafx.fxml;
    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app;

    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller;
    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller to javafx.fxml;
    exports edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog;
    opens edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog to javafx.fxml;
>>>>>>> f7cf646124ad67dfc09201e791b3ff2b36b63b37
}