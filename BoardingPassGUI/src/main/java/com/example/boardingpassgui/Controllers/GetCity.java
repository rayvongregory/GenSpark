package com.example.boardingpassgui.Controllers;

import com.amadeus.Params;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.example.boardingpassgui.BoardingPassGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.boardingpassgui.BoardingPassGUI.amadeus;

public class GetCity {
    @FXML
    private Button button;
    @FXML
    private VBox vbox;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private TextField textField;

    public static Location cityOfDeparture;
    public static Location cityOfArrival;

    @FXML
    public void initialize() {
        if (cityOfDeparture == null) {
            label1.setText("Where are you departing from?");
            label2.setText("Please enter the name of the city you'll be departing from.");
            textField.setPromptText("City of departure");
        } else {
            Label label3 = new Label("Departing from: " + cityOfDeparture.getName() + " - " + cityOfDeparture.getIataCode());
            Button btn = new Button("Edit");
            btn.setOnMouseClicked(e-> {
                cityOfDeparture = null;
                try {
                    reloadScene();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            HBox hbox = new HBox(label3, btn);
            HBox.setMargin(label3, new Insets(0, 10, 0, 0));
            hbox.setAlignment(Pos.CENTER);
            hbox.setStyle("-fx-pref-width: 350;\n" +
                          "-fx-max-width: 350;");
            vbox.getChildren().add(1, hbox);
            label1.setText("Where are you going?");
            label2.setText("Please enter the name of the city you're going to.");
            textField.setPromptText("City of arrival");
        }
    }

    @FXML
    private void getCities() {
        Location[] cities = getCities(textField.getText());
        if (cities.length == 0) {
            Label label = new Label("Sorry, there are no airports at this location. Please try another city.");
            BoardingPassGUI.issueWithInput(vbox, label, button);
            return;
        }
        displayCities(cities);
    }

    public static Location[] getCities(String city) { // api call for retrieving all airports
        if(city.length() == 0) {
            return new Location[0];
        } else {
            try {
                return amadeus.referenceData.locations.get(Params
                        .with("keyword", city)
                        .and("subType", Locations.ANY));
            } catch (Exception e) {
                return new Location[0];
            }
        }
    }

    private void displayCities(Location[] locations) {
        Node title = vbox.getChildren().get(0);
        HBox hbox = new HBox();
        if(cityOfDeparture == null) label1.setText("Select your city/airport of departure.");
        else {
            Label label3 = new Label("Departing from: " + cityOfDeparture.getName() + " - " + cityOfDeparture.getIataCode());
            Button btn = new Button("Edit");
            btn.setOnMouseClicked(e-> {
                cityOfDeparture = null;
                try {
                    reloadScene();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            hbox = new HBox(label3, btn);
            HBox.setMargin(label3, new Insets(0, 10, 0, 0));
            hbox.setAlignment(Pos.CENTER);
            hbox.setStyle("-fx-pref-width: 350;\n" +
                    "-fx-max-width: 350;");
            label1.setText("Select your city/airport of arrival.");
        }

        vbox.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        vbox.getChildren().addAll(title,label1, scrollPane, button);
        if(cityOfDeparture != null) vbox.getChildren().add(1, hbox);

        VBox.setMargin(button, new Insets(10, 0, 0, 0));
        button.setText("I don't see the city/airport I'm looking for");
        button.setOnMouseClicked(e-> {
            try {
                this.reloadScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        GridPane gridPane = new GridPane();
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        cc1.setPercentWidth(50);
        cc2.setPercentWidth(50);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10.0);
        gridPane.setVgap(10.0);
        scrollPane.setMaxWidth(700);
        gridPane.getColumnConstraints().addAll(cc1, cc2);
        gridPane.setStyle("-fx-border-color: transparent;");
        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        printLocations(gridPane, locations, this);
    }

    public static void printLocations(GridPane gridPane, Location[] locations, GetCity getCity) {
        int col = 0;
        int row = 0;
        for(int i = 0; i < locations.length; i++) {
            Label label = new Label(String.format("%s - %s",locations[i].getDetailedName(), locations[i].getIataCode()));
            Button button = new Button("Select");
            button.setStyle("-fx-background-color:#d1d1d1; -fx-border-radius: 0;");
            button.setCursor(Cursor.HAND);
            HBox hBox = new HBox(label, button);
            HBox.setMargin(label, new Insets(0, 10, 0, 0));
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setStyle("-fx-border-color: #d1d1d1;\n" +
                          "-fx-background-color:white;\n" +
                          "-fx-pref-width: 350;");
            int finalI = i;
            button.setOnMouseClicked(e-> {
                if(GetCity.cityOfDeparture == null) {
                    GetCity.cityOfDeparture = locations[finalI];
                    try {
                        getCity.reloadScene();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    GetCity.cityOfArrival = locations[finalI];
                    try {
                        getCity.setDepartureDate();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            gridPane.add(hBox, col, row);
            if(col == 1) {
                col = 0;
                row++;
            } else col++;
        }
    }

    public void reloadScene() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-city.fxml")));
        stage.setScene(new Scene(root));
    }


    public void setDepartureDate() throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(BoardingPassGUI.class.getResource("get-departure-date.fxml")));
        stage.setScene(new Scene(root));
    }
}
