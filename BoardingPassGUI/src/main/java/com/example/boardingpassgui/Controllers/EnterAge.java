package com.example.boardingpassgui.Controllers;

import com.example.boardingpassgui.BoardingPassGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Objects;

public class EnterAge {

    @FXML
    private Button button;
    @FXML
    private VBox vbox;
    @FXML
    private TextField textField;

    public static int clientAge;
    public static final HashMap<String, Integer> numOfAdultsAndChildren = new HashMap<>(){{
        put("adults", 0);
        put("children", 0);
    }};

    @FXML
    protected void initialize() {
        vbox.getChildren().add(1, EnterName.niceToMeetYouLabel);
        VBox.setMargin(EnterName.niceToMeetYouLabel, new Insets(0,0,30,0));
    }

    @FXML
    public void validateAge() throws IOException {
        String age = textField.getText();
        if(isInvalidAge(age)) {
            Label label = new Label("Sorry, there was an issue with your input. Please enter your age.");
            BoardingPassGUI.issueWithInput(vbox, label, button);
            return;
        }
        clientAge = Integer.parseInt(age);
        if(clientAge <= 12) numOfAdultsAndChildren.put("children", 1);
        else numOfAdultsAndChildren.put("adults", 1);
        getCityOfDeparture();
    }

    public static boolean isInvalidAge(String age) {
        try {
            int ageAsInt = Integer.parseInt(age);
            if(ageAsInt < 0 || ageAsInt > 122) throw new InputMismatchException(); // oldest person in recent history was 122 years old
            return false;
        } catch (NumberFormatException | InputMismatchException e) {
            return true;
        }
    }

    public void getCityOfDeparture() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-city.fxml")));
        stage.setScene(new Scene(root));
    }

}


