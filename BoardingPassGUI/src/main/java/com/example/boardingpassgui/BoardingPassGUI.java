package com.example.boardingpassgui;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import com.amadeus.Amadeus;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class BoardingPassGUI extends Application {

    public static final Amadeus amadeus = Amadeus
            .builder("kDDUZUezgEPkoRPtafrcIBUl0dopysJy", "sVO6iVdb3flLMGVn") // API KEY INFO, WILL SECURE LATER
            .build();

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    static String validateYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        try {
            String response = scanner.nextLine().toUpperCase();
            if(!response.equals("Y") && !response.equals("N")) {
                throw new InputMismatchException();
            } else {
                return response;
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter Y or N.");
            return validateYesOrNo();
        }
    }

    public static void issueWithInput(VBox vbox, Label label, Button button) {
        vbox.getChildren().add(label);
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Paint.valueOf("darkred"));
        label.setPadding(new Insets(5,0,0,0));
        button.setDisable(true);
        delay(5000, ()-> {
            vbox.getChildren().remove(label);
            button.setDisable(false);
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("enter-name.fxml")));
        Scene scene = new Scene(root, Color.DARKGRAY);
        stage.setTitle("Boarding Pass Program");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static class GetPhoneNumber {
        @FXML
        private Button button;

        @FXML
        public void goNext(MouseEvent mouseEvent) throws IOException {
            Stage stage = (Stage) button.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("get-gender.fxml"));
            stage.setTitle("Please enter your phone number");
            stage.setScene(new Scene(root));
        }
    }

    public static class EnterMonth {
        @FXML
        private Button button;

        @FXML
        public void goNext(MouseEvent mouseEvent) throws IOException {
            Stage stage = (Stage) button.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("enter-day.fxml"));
            stage.setTitle("enter the month of departure ");
            stage.setScene(new Scene(root));
        }
    }
}