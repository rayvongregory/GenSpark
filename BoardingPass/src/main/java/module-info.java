module com.main.boardingpass {
    requires javafx.controls;
    requires javafx.fxml;
    requires amadeus.java;
    requires com.google.gson;
    requires commons.validator;
    requires org.apache.commons.io;


    opens com.main.boardingpass to javafx.fxml;
    exports com.main.boardingpass;
}