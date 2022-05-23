package com.example.boardingpassgui.Controllers;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.boardingpassgui.BoardingPassGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.boardingpassgui.BoardingPassGUI.amadeus;

public class GetFlights {

    @FXML
    private VBox innerVBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button button;
    @FXML
    private VBox vbox;
    public static FlightOfferSearch clientFlight;


    @FXML
    public void initialize() {
        FlightOfferSearch[] flights = getFlights();
        if (flights.length == 0) {
            vbox.getChildren().remove(scrollPane);
            Label label3 = new Label("Sorry, there aren't any flights going to " + GetCity.cityOfDeparture.getName() + " on the date you specified.\nTry another search");
            vbox.getChildren().add(2, label3);
            return;
        }

        for (int i = 0; i < flights.length; i++) {
            FlightOfferSearch flight = flights[i];
            String tripTotalPrice = flight.getPrice().getGrandTotal();
            FlightOfferSearch.Itinerary[] flightItineraries = flight.getItineraries();
            for (FlightOfferSearch.Itinerary itinerary : flightItineraries) {
                String tripDuration = itinerary.getDuration();
                FlightOfferSearch.SearchSegment[] searchSegments = itinerary.getSegments();
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < searchSegments.length; j++) {
                    String departingFrom = searchSegments[j].getDeparture().getIataCode();
                    String departingAt = searchSegments[j].getDeparture().getAt();
                    String flyingTo = searchSegments[j].getArrival().getIataCode();
                    String arrivingAt = searchSegments[j].getArrival().getAt();
                    stringBuilder.append("Departing from: ").append(departingFrom).append("\n")
                            .append("Departing at: ").append(departingAt).append("\n")
                            .append("Arriving to: ").append(flyingTo).append("\n")
                            .append("Arriving at: ").append(arrivingAt).append("\n\n");
                }
                stringBuilder.append("Trip duration: ").append(tripDuration).append("\n")
                        .append("Trip cost: ").append("$").append(tripTotalPrice);
                Text text = new Text(stringBuilder.toString());
                Button btn = new Button("Book this flight");
                int finalI = i;
                btn.setOnMouseClicked(e-> {
                    clientFlight = flights[finalI];
                    try {
                        getAdditionalInformation();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                VBox vb = new VBox(text, btn);
                vb.setAlignment(Pos.CENTER);
                if(i == 0) VBox.setMargin(vb, new Insets(20, 0, 10, 0));
                else if (i == flights.length-1) VBox.setMargin(vb, new Insets(10, 0, 20, 0));
                else VBox.setMargin(vb, new Insets(10, 0, 10, 0));
                innerVBox.getChildren().add(vb);
            }
        }

    }

    @FXML
    public void tryAnotherSearch() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-city.fxml")));
        stage.setScene(new Scene(root));
    }

    public FlightOfferSearch[] getFlights() { // gets all flights that match user's query
        try {
            return amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", GetCity.cityOfDeparture.getIataCode())
                            .and("destinationLocationCode", GetCity.cityOfArrival.getIataCode())
                            .and("departureDate", GetDepartureDate.dateOfDeparture)
                            .and("adults", EnterAge.numOfAdultsAndChildren.get("adults"))
                            .and("children", EnterAge.numOfAdultsAndChildren.get("children")));
        } catch (ResponseException e) {
            Label label = new Label("Sorry, something went wrong retrieving your results. Please try again.");
            Button button = new Button("Search again");
            vbox.getChildren().add(button);
            BoardingPassGUI.issueWithInput(vbox, label, button);
            return new FlightOfferSearch[0];
        }
    }

    public void getAdditionalInformation() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-additional-information.fxml")));
        stage.setScene(new Scene(root));
    }
}
