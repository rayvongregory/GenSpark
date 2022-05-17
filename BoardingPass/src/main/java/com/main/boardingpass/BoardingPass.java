package com.main.boardingpass;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;


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
    private static String clientEmail;
    private static String clientPhoneNumber;
    private static char clientGender;
    private static FlightOfferSearch clientFlight;
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

    public static void main(String[] args) throws IOException {
//        launch();
        getGender();
        System.exit(0);
        System.out.println("Boarding Pass\n\n");
        clientName = getName();
        System.out.printf("We're glad you chose to book your trip with us today, %s.%n", clientName.split(" ")[0]);
        clientAge = getAge();
        search();
        // we may want to move some of these functions to another class for readability
    }

    private static void search() throws IOException {
        gatherInformation();
        getFlight();
        gatherAdditionalInformation();
        bookTrip();
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

        if(validateYesOrNo().equals("Y")) return selectCity(cities);
        else {
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

    private static void getFlight() throws IOException {
        System.out.printf("Here are the flights%nFROM: %s - %s%nTO: %s - %s%n",
                cityOfDeparture.getDetailedName(), cityOfDeparture.getIataCode(),
                cityOfArrival.getDetailedName(), cityOfArrival.getIataCode());
        FlightOfferSearch[] flights = getFlights();
        if(flights.length == 0) {
            System.out.print("Sorry, there aren't any flights going to " + cityOfDeparture.getName() + " on the date you specified.\nWould you like to try another search? (Y/N)\nResponse: )");
            if(validateYesOrNo().equals("Y")) {
                search();
                return;
            } else {
                System.out.println("I'm sorry we weren't able to assist you today. Goodbye.");
                System.exit(0);
            }
        }

        for(FlightOfferSearch flight: flights) {
            String tripTotalPrice = flight.getPrice().getGrandTotal();
            FlightOfferSearch.Itinerary[] flightItineraries = flight.getItineraries();
            for(FlightOfferSearch.Itinerary itinerary: flightItineraries) {
                String tripDuration = itinerary.getDuration();
                FlightOfferSearch.SearchSegment[] searchSegments = itinerary.getSegments();
                printFlights(searchSegments);
                System.out.println("Trip duration: " + tripDuration);
                System.out.println("Trip cost: " + tripTotalPrice);
            }
        }
        System.out.print("Would you like to book any of these trips today? (Y/N)\nResponse: ");
        if(validateYesOrNo().equals("Y")) {
            selectFlight(flights);
        } else {
            System.out.println("We're sorry you didn't find what you were looking for. Please try another search.");
            search();
        }
    }

    private static void selectFlight(FlightOfferSearch[] flights) {
        System.out.print("Enter the number associated with your choice.\nResponse: ");
        try {
            int flight = Integer.parseInt(scanner.nextLine());
            clientFlight = flights[flight-1];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            issueWithInput();
            selectFlight(flights);
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

    private static void gatherAdditionalInformation() {
        clientEmail = getEmail();
        clientPhoneNumber = getPhoneNumber();
        clientGender = getGender();
    }

    private static String getEmail() {
        System.out.print("Enter your email address.\nEmail: ");
        String email = scanner.nextLine().trim();
        if(isInvalidEmail(email)) {
            issueWithInput();
            return getEmail();
        } else return email;
    }

    private static String getPhoneNumber() {
        System.out.print("Enter your phone number in XXX-XXX-XXXX format.\nPhone Number: ");
        String phoneNumber = scanner.nextLine();
        if(isInvalidPhoneNumber(phoneNumber)) {
            issueWithInput();
            return getPhoneNumber();
        } else return phoneNumber;
    }

    private static char getGender() {
        System.out.print("Enter M if you are man or W if you are a woman.\nGender: ");
        String gender = scanner.nextLine().toUpperCase();
        if(isUnrecognizedGender(gender)) {
            issueWithInput();
            return getGender();
        } else return gender.charAt(0);
    }

    private static void bookTrip() throws IOException {
        double clientTripCost = addDiscountIfApplicable(Double.parseDouble(clientFlight.getPrice().getGrandTotal()), clientAge, clientGender);
        BookedTrip bookedTrip = new BookedTrip(clientName, clientAge, clientEmail, clientPhoneNumber, clientGender,
                clientFlight, cityOfDeparture, cityOfArrival, departureDate, clientTripCost);
        bookedTrip.generateBoardingPass();
        bookedTrip.getBoardingPass();
    }

    public static double addDiscountIfApplicable(double costBeforeDiscount, int clientAge, char clientGender) {
        DecimalFormat df = new DecimalFormat("0.00");
        if(clientAge <= 12) return Double.parseDouble(df.format(costBeforeDiscount * 0.5));
        if(clientAge >= 60) return Double.parseDouble(df.format(costBeforeDiscount * 0.4));
        if(clientGender == 'F') return Double.parseDouble(df.format(costBeforeDiscount * 0.75));
        return costBeforeDiscount;
    }

    // HELPER FUNCTIONS & TESTABLE FUNCTIONS //

    private static void printFlights(FlightOfferSearch.SearchSegment[] searchSegments) {
        for(int i = 1; i < searchSegments.length; i++) {
            String departingFrom = searchSegments[i-1].getDeparture().toString();
            String departingAt = searchSegments[i-1].getDeparture().getAt();
            String flyingTo = searchSegments[i-1].getArrival().toString();
            String arrivingAt = searchSegments[i-1].getArrival().getAt();
            System.out.printf("%-2s. Departing from: %s\n%-2s Departing at: %s\n%-2s Arriving to: %s\n%-2s Arriving at: %s",
                    i, departingFrom, "", departingAt, "", flyingTo, "", arrivingAt);
        }
    }

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
}