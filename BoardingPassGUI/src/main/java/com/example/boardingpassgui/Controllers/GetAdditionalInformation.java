package com.example.boardingpassgui.Controllers;

import com.example.boardingpassgui.BoardingPassGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.util.Objects;

public class GetAdditionalInformation {
    @FXML
    private VBox vbox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField genderField;
    @FXML
    private Button button;

    public static String clientEmail;
    public static String clientPhoneNumber;
    public static char clientGender;

    public void validateAdditionalInfo() throws IOException {
        clientEmail = emailField.getText();
        clientPhoneNumber = phoneNumberField.getText();
        if(isInvalidEmail(clientEmail)) {
            Label label3 = new Label("There appears to be a mistake in your email address.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        if(isInvalidPhoneNumber(clientPhoneNumber)) {
            Label label3 = new Label("There appears to be a mistake in your phone number. Please enter your phone number in XXX-XXX-XXXX format, dashes included.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        if(isUnrecognizedGender(genderField.getText())) {
            Label label3 = new Label("Sorry, we do not recognize that gender. Please enter M if you are man or W if you are a woman.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        clientGender = genderField.getText().charAt(0);
        bookTrip();
    }
    public static boolean isInvalidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return !validator.isValid(email);
    }

    public static boolean isInvalidPhoneNumber(String phoneNumber) {
        return !phoneNumber.matches("[0-9]{3}-[0-9]{3}-[0-9]{4}");
    }

    public static boolean isUnrecognizedGender(String gender) {
        if(gender.length() != 1) return true;
        char g = gender.charAt(0);
        return g != 'M' && g != 'W';
    }

    public void bookTrip() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("generate-boarding-pass.fxml")));
        stage.setScene(new Scene(root));
    }
}
