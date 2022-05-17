package com.main.boardingpass;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.amadeus.shopping.FlightOffers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import com.amadeus.Amadeus;
import com.amadeus.Params;

import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import java.time.LocalDateTime;

public class BoardingPass extends Application {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Amadeus amadeus = Amadeus
            .builder("kDDUZUezgEPkoRPtafrcIBUl0dopysJy", "sVO6iVdb3flLMGVn") // API KEY INFO, WILL SECURE LATER
            .build();
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
    private static String clientName;
    private static int clientAge;
    private static final HashMap<String, Integer> numOfAdultsAndChildren = new HashMap<>(){{
        put("adults", 0);
        put("children", 0);
    }};
    private static Location cityOfDeparture;
    private static Location cityOfArrival;
    private static String departureDate;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BoardingPass.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        launch();
        System.out.println("Boarding Pass\n\n");
        clientName = getName();
        clientAge = getAge();
        System.out.printf("We're glad you chose to book your trip with us today, %s.%n", clientName.split(" ")[0]);
        gatherInformation();
        selectFlight();
        // we may want to move some of these functions to another class for readability
    }

    private static void gatherInformation() {
        cityOfDeparture = getCity('D');
        cityOfArrival = getCity('A');
        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentYear = currentDateTime.getYear();
        int currentMonth = currentDateTime.getMonthValue();
        int currentDate = currentDateTime.getDayOfMonth();
        int yearOfTravel = getYearOfTravel(currentYear);
        int monthOfTravel = getMonthOfTravel(currentMonth, currentYear, yearOfTravel);
        int dateOfTravel = getDateOfTravel(currentDate, currentMonth, monthOfTravel);
        departureDate = getDepartureDate(yearOfTravel, monthOfTravel, dateOfTravel);
    }

    private static String getName() {
        System.out.print("Please enter your first and last names.\nFull Name: ");
        String name = scanner.nextLine().trim();
        if(isInvalidName(name)) {
            issueWithInput();
            return getName();
        }
        return name;
    }

    private static int getAge() {
        System.out.print("Please enter your age.\nAge: ");
        String age = scanner.nextLine().trim();
        if(isInvalidAge(age)) {
            issueWithInput();
            return getAge();
        }
        int ageAsInt = Integer.parseInt(age);
        if(ageAsInt <= 12) numOfAdultsAndChildren.put("children", 1);
        else numOfAdultsAndChildren.put("adults", 1);
        return ageAsInt;
    }

    private static Location[] getCities(Character D_or_A ) {
        if (D_or_A == 'D') System.out.print("Please enter the name of the city you'll be departing from.\nDeparting from: ");
        else System.out.print("Please enter the name of the city you're going to.\nGoing to: ");

        String city = scanner.nextLine().trim();
        if(city.length() == 0) {
            issueWithInput();
            return getCities(D_or_A);
        } else {
            try {
                return amadeus.referenceData.locations.get(Params
                        .with("keyword", city)
                        .and("subType", Locations.ANY));
            } catch (Exception e) {
                timedOut();
                return getCities(D_or_A);
            }
        }
    }

    private static Location getCity(Character D_or_A) {
        Location[] cities = getCities('D');
        if(cities.length > 0) printLocations(cities);
        else {
            System.out.println("There are no results for your request. Please try another city.");
            return getCity(D_or_A);
        }
        if(D_or_A == 'D') System.out.print("Do you see the airport you wish to depart from? (Y/N)\nResponse: ");
        else System.out.print("Do you see the airport you wish to go to? (Y/N)\nResponse: ");

        if(validateYesOrNo().equals("Y")) {
            return selectCity(cities);
        } else {
            System.out.println("We're sorry you didn't find what you were looking for. Please try another search.");
            return getCity(D_or_A);
        }
    }

    private static Location selectCity(Location[] cities) {
        System.out.print("Enter the number associated with your choice.\nResponse: ");
        try {
            int city = Integer.parseInt(scanner.nextLine());
            return cities[city-1];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            issueWithInput();
            return selectCity(cities);
        }
    }

    private static void selectFlight() {
        System.out.printf("Here are the flights%nFROM: %s - %s%nTO: %s - %s%n",
                cityOfDeparture.getDetailedName(), cityOfDeparture.getIataCode(),
                cityOfArrival.getDetailedName(), cityOfArrival.getIataCode());
        FlightOfferSearch[] flights = getFlights();
        if(flights.length == 0) {
            // IF THERE AREN'T ANY FLIGHTS, ASK CLIENT IF THEY WANT TO PERFORM ANOTHER SEARCH OR EXIT THE APP
            return;
        }

        for(FlightOfferSearch flight : flights) {
            String flightTotalPrice = flight.getPrice().getGrandTotal();
            FlightOfferSearch.Itinerary[] flightItineraries = flight.getItineraries();
            for(FlightOfferSearch.Itinerary itinerary : flightItineraries) {
                String flightDuration = itinerary.getDuration();
                FlightOfferSearch.SearchSegment[] searchSegments = itinerary.getSegments();
                for(FlightOfferSearch.SearchSegment searchSegment : searchSegments) {
                    String departingFrom = searchSegment.getDeparture().toString();
                    String departingAt = searchSegment.getDeparture().getAt();
                    String flyingTo = searchSegment.getArrival().toString();
                    String arrivingAt = searchSegment.getArrival().getAt();

                    // format and display all flight information
                    // user picks the flight they want
                    // user enters String email, String phone number, and char gender
                    // if gender == 'F', provide 25% discount iff they don't qualify for the other discounts

                }
            }
        }
    }

    private static FlightOfferSearch[] getFlights() {
        try {
            return amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", cityOfDeparture.getIataCode())
                            .and("destinationLocationCode", cityOfArrival.getIataCode())
                            .and("departureDate", departureDate)
                            .and("adults", numOfAdultsAndChildren.get("adults"))
                            .and("children", numOfAdultsAndChildren.get("children")));
        } catch (ResponseException e) {
            timedOut();
            return getFlights();
        }
    }

    private static int getYearOfTravel(int currentYear) {
        System.out.print("What is the year of departure? Type in YYYY format.\nYear: ");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            if(isInvalidYear(year, currentYear)) throw new InputMismatchException();
            else return year;
        } catch (NumberFormatException | InputMismatchException e) {
            issueWithInput();
            return getYearOfTravel(currentYear);
        }
    }

    private static int getMonthOfTravel(int currentMonth, int currentYear, int yearOfTravel) {
        System.out.print("What is the month of departure? Type 1 for January, 2 for February, etc.\nMonth: ");
        try {
            int month = Integer.parseInt(scanner.nextLine());
            if(isInvalidMonth(month, currentMonth, currentYear, yearOfTravel)) throw new InputMismatchException();
            else return month;
        } catch (NumberFormatException | InputMismatchException e) {
            issueWithInput();
            return getMonthOfTravel(currentMonth, currentYear, yearOfTravel);
        }
    }

    private static int getDateOfTravel(int currentDayOfTheMonth, int currentMonth, int monthOfTravel) {
        System.out.print("What is the day of departure? Type 1 for first of month, 2 for the second of the month, etc.\nDay: ");
        try {
            int date = Integer.parseInt(scanner.nextLine());
            if(isInvalidDate(date, currentDayOfTheMonth, monthOfTravel, currentMonth)) throw new InputMismatchException();
            else return date;
        } catch (NumberFormatException | InputMismatchException e) {
            issueWithInput();
            return getDateOfTravel(monthOfTravel, currentMonth, currentDayOfTheMonth);
        }
    }

    // HELPER FUNCTIONS //

    private static void printLocations(Location[] locations) {
        for(int i = 1; i < locations.length; i++) {
            System.out.printf("%s. %s - %s%n", i, locations[i-1].getDetailedName(), locations[i-1].getIataCode());
        }
    }

    private static void issueWithInput() {
        System.out.println("Sorry, there was an issue with your input. Please try again.");
    }

    private static void timedOut() {
        System.out.println("Sorry, your response timed out. Please try again.");
    }

    private static String validateYesOrNo() {
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

    private static void checkForLeapYearAddFebruary() {
        int numDaysInCurrentYear = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR);
        if(numDaysInCurrentYear > 365) validDates.put(2, 29);
        else validDates.put(2, 28);
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

    public static boolean isInvalidAge(String age) {
        try {
            int ageAsInt = Integer.parseInt(age);
            if(ageAsInt < 0 || ageAsInt > 122) throw new InputMismatchException(); // oldest person in recent history was 122 years old
            return false;
        } catch (NumberFormatException | InputMismatchException e) {
            return true;
        }
    }

    public static boolean isInvalidYear(int year, int currentYear) {
        return year < currentYear || year > currentYear + 1;
    }

    public static boolean isInvalidMonth(int month, int currentMonth, int currentYear, int yearOfTravel) {
        return month < 1 || month > 12 || (currentYear == yearOfTravel && month < currentMonth);
    }

    public static boolean isInvalidDate (int date, int currentDayOfTheMonth, int monthOfTravel, int currentMonth) {
        checkForLeapYearAddFebruary();
        return date < 1 || date > validDates.get(monthOfTravel) || (monthOfTravel == currentMonth && date < currentDayOfTheMonth);
    }

    public static String getDepartureDate(int yearOfTravel, int monthOfTravel, int dateOfTravel) { // converts data to YYYY-MM-DD format for http request
        return String.format("%s-%02d-%02d", yearOfTravel, monthOfTravel, dateOfTravel); // %02d ensures that month and date are 2 digits
    }
}