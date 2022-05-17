module com.main.boardingpass {
    requires javafx.controls;
    requires javafx.fxml;
    requires amadeus.java;
    requires com.google.gson;


    opens com.main.boardingpass to javafx.fxml;
    exports com.main.boardingpass;
}