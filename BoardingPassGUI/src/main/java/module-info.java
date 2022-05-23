module com.example.boardingpassgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires amadeus.java;
    requires org.apache.commons.io;
    requires commons.validator;


    opens com.example.boardingpassgui to javafx.fxml;
    exports com.example.boardingpassgui;
    exports com.example.boardingpassgui.Controllers;
    opens com.example.boardingpassgui.Controllers to javafx.fxml;
}