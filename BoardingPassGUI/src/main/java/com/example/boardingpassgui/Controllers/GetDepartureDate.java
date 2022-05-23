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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Objects;

public class GetDepartureDate {
    private static final HashMap<Integer, Integer> validDates = new HashMap<>(){
        {
            put(1, 31);
            put(3, 31);
            put(4, 30);
            put(5, 31);
            put(6, 30);
            put(7, 31);
            put(8, 31);
            put(9, 30);
            put(10, 31);
            put(11, 30);
            put(12, 31);
        }
    }; // Number of days in each month, February is added later

    @FXML
    private VBox vbox;
    @FXML
    public Button edit1;
    @FXML
    public Button edit2;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private TextField textField;
    @FXML
    private Button button;
    public static String dateOfDeparture;
    private static final LocalDateTime currentDateTime = LocalDateTime.now();
    private static final int currentYear = currentDateTime.getYear();
    private static final int currentMonth = currentDateTime.getMonthValue();
    private static final int currentDate = currentDateTime.getDayOfMonth();

    @FXML
    public void initialize() {
        label1.setText("Departing from: " + GetCity.cityOfDeparture.getName() + " - " + GetCity.cityOfDeparture.getIataCode());
        label2.setText("Arriving at: " + GetCity.cityOfArrival.getName() + " - " + GetCity.cityOfArrival.getIataCode());
        checkForLeapYearAddFebruary();
    }


    @FXML
    public void validateDate() throws IOException {
        String date = textField.getText();
        String[] yearMonthDate = date.split("-");

        if(yearMonthDate.length != 3) {
            Label label3 = new Label("Sorry, there is an issue with the date you entered. Please enter your desired departure date in YYYY-MM-DD format.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        String yearOfTravel = yearMonthDate[0];
        if(isInvalidYear(yearOfTravel)) {
            Label label3 = new Label("Sorry, there is an issue with the year you've entered. It has already passed, is too far into the future, or is not a valid 4-digit year.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        String monthOfTravel = yearMonthDate[1];
        if(isInvalidMonth(monthOfTravel, yearOfTravel)) {
            Label label3 = new Label("Sorry, there is an issue with the month you've entered. It has either already passed or is not a valid 2-digit month.");
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        String dateOfTravel = yearMonthDate[2];
        if(isInvalidDate(dateOfTravel, monthOfTravel)) {
            Label label3 = new Label("Sorry, there is an issue with the date you've entered. It has either already passed or is not a valid 2-digit date for the date you have chosen.");
            label3.setWrapText(true);
            BoardingPassGUI.issueWithInput(vbox, label3, button);
            return;
        }
        dateOfDeparture = String.format("%s-%s-%s", yearOfTravel, monthOfTravel, dateOfTravel);
        getFlights();
    }


    private static void checkForLeapYearAddFebruary() {
        int numDaysInCurrentYear = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR);
        if(numDaysInCurrentYear > 365) validDates.put(2, 29);
        else validDates.put(2, 28);
    }

    public static boolean isInvalidYear(String yearAsString) {
        if(yearAsString.length() != 4) return true;
        try {
            int year = Integer.parseInt(yearAsString);
            if(year < currentYear || year > currentYear + 1) throw new InputMismatchException();
            else return false;
        } catch (NumberFormatException | InputMismatchException e) {
            return true;
        }
    }

    public static boolean isInvalidMonth(String monthAsString, String yearOfTravel) {
        if(monthAsString.length() != 2) return true;
        try {
            int month = Integer.parseInt(monthAsString);
            if(month < 1 || month > 12 || (currentYear == Integer.parseInt(yearOfTravel) && month < currentMonth)) throw new InputMismatchException();
            else return false;
        } catch (NumberFormatException | InputMismatchException e) {
            return true;
        }
    }

    public static boolean isInvalidDate(String dateAsString, String monthOfTravel) {
        if(dateAsString.length() != 2) return true;
        try {
            int date = Integer.parseInt(dateAsString);
            if(date < 1 || date > validDates.get(Integer.parseInt(monthOfTravel)) || (Integer.parseInt(monthOfTravel) == currentMonth && date < currentDate)) throw new InputMismatchException();
            else return false;
        } catch (NumberFormatException | InputMismatchException e) {
            return true;
        }
    }


    @FXML
    public void editCityOfDeparture() {
        GetCity.cityOfArrival = null;
        GetCity.cityOfDeparture = null;
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-city.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stage.setScene(new Scene(Objects.requireNonNull(root)));
    }

    @FXML
    public void editCityOfArrival() {
        GetCity.cityOfDeparture = null;
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-city.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assert root != null;
        stage.setScene(new Scene(root));
    }

    public void getFlights() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-flights.fxml")));
        stage.setScene(new Scene(root));
    }
}
