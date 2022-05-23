package com.example.boardingpassgui.Controllers;

import com.example.boardingpassgui.BookedTrip;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DecimalFormat;

public class GenerateBoardingPass {
    @FXML
    private VBox vbox;
    @FXML
    private Label label;
    @FXML
    private Text boardingPass;

    @FXML
    public void initialize() throws IOException {
        double clientTripCost = addDiscountIfApplicable(Double.parseDouble(GetFlights.clientFlight.getPrice().getGrandTotal()), EnterAge.clientAge, GetAdditionalInformation.clientGender);
        BookedTrip bookedTrip = new BookedTrip(EnterName.clientName, EnterAge.clientAge, GetAdditionalInformation.clientEmail, GetAdditionalInformation.clientPhoneNumber, GetAdditionalInformation.clientGender,GetFlights.clientFlight,GetCity.cityOfDeparture, GetCity.cityOfArrival, GetDepartureDate.dateOfDeparture, clientTripCost);
        bookedTrip.generateBoardingPass();
        label.setText("You're all set, " + EnterName.clientName.split(" ")[0] + "! Here is your boarding pass:\n\n");
        boardingPass.setText(bookedTrip.getBoardingPass());
    }

    public static double addDiscountIfApplicable(double costBeforeDiscount, int clientAge, char clientGender) {
        DecimalFormat df = new DecimalFormat("0.00");
        if(clientAge <= 12) return Double.parseDouble(df.format(costBeforeDiscount * 0.5));
        if(clientAge >= 60) return Double.parseDouble(df.format(costBeforeDiscount * 0.4));
        if(clientGender == 'F') return Double.parseDouble(df.format(costBeforeDiscount * 0.75));
        return costBeforeDiscount;
    }

    public void getBoardingPass() {
    }
}
