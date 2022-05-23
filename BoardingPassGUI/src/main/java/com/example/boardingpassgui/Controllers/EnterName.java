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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class EnterName {

    @FXML
    private Button button;
    @FXML
    private TextField textField; // note to rayvon, do NOT make these static
    @FXML
    private VBox vbox;

    public static String clientName;
    public static Label niceToMeetYouLabel = new Label();

    @FXML
    public void validateName() throws IOException {
        clientName = textField.getText();
        if(isInvalidName(clientName)) {
            Label label = new Label("Sorry, there was an issue with your input. Please enter your first and last name.");
            BoardingPassGUI.issueWithInput(vbox, label, button);
            return;
        }
        getAge();
    }

    public void getAge() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("enter-age.fxml")));
        stage.setScene(new Scene(root));
        niceToMeetYouLabel.setText("Nice to meet you, " + EnterName.clientName.split(" ")[0]+ "!");
        niceToMeetYouLabel.setPrefWidth(600.0);
        niceToMeetYouLabel.setPrefHeight(60.0);
        niceToMeetYouLabel.setWrapText(true);
        niceToMeetYouLabel.setAlignment(Pos.CENTER);
        niceToMeetYouLabel.setTextAlignment(TextAlignment.CENTER);
        niceToMeetYouLabel.setFont(new Font("", 16.0));
    }

    public static boolean isInvalidName(String s) {
        if(s.contains("  ") || s.contains("--") || s.contains("''") ||
                s.contains(" -") || s.contains("- ") || s.contains("' ") ||
                s.contains(" '") || s.contains("-'") || s.contains("'-") || s.length() < 3) return true;
        String filteredWord = Arrays.toString(Arrays.stream(s.split("")).filter(c-> {
            char ch = c.charAt(0);
            return Character.isLetter(ch) || ch == '-' || ch == ' ' || ch == '\'';
        }).toArray());
        return filteredWord.length() == s.length() || s.split(" ").length < 2;
    }




}