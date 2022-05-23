package com.example.boardingpassgui;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;

public class BookedTrip {
    private final String clientName;
    private final int clientAge;
    private final String clientEmail;
    private final String clientPhoneNumber;
    private final char clientGender;
    private final FlightOfferSearch clientFlight;
    private final double clientTotalCost;
    private final Location cityOfDeparture;
    private final Location cityOfArrival;
    private final String departureDate;
    private String boardingNumber;
    private static final HashSet<String> boardingNumbers = new HashSet<>();

    public BookedTrip(String clientName, int clientAge, String clientEmail, String clientPhoneNumber,
                      char clientGender, FlightOfferSearch clientFlight, Location cityOfDeparture,
                      Location cityOfArrival, String departureDate, double clientTotalCost) {
        this.clientName = clientName;
        this.clientAge = clientAge;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientGender = clientGender;
        this.clientFlight = clientFlight;
        this.cityOfDeparture = cityOfDeparture;
        this.cityOfArrival = cityOfArrival;
        this.departureDate = departureDate;
        this.clientTotalCost = clientTotalCost;
    }

//    public String getClientName() {
//        return clientName;
//    }
//
//    public int getClientAge() {
//        return clientAge;
//    }
//
//    public String getClientEmail() {
//        return clientEmail;
//    }
//
//    public String getClientPhoneNumber() {
//        return clientPhoneNumber;
//    }
//
//    public char getClientGender() {
//        return clientGender;
//    }
//
//    public FlightOfferSearch getClientFlight() { return clientFlight; }
//
//    public double getClientTotalCost() { return clientTotalCost; }

    private String generateBoardingNumber() {
        String[] names = clientName.split(" ");
        String randomNumbers = generateSixRandomNumbers();
        String boardingNumber = String.format("%s%s-%s", names[0].charAt(0), names[1].charAt(0), randomNumbers);
        if(boardingNumbers.contains(boardingNumber)) return generateBoardingNumber();
        else {
            boardingNumbers.add(boardingNumber);
            this.boardingNumber = boardingNumber;
            return boardingNumber;
        }
    }

    private String generateSixRandomNumbers() {
        Random r = new Random();
        StringBuilder sixRandomNumbers = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            sixRandomNumbers.append(r.nextInt(10));
        }
        return sixRandomNumbers.toString();
    }

    public void generateBoardingPass() throws IOException {
        String boardingNumber = generateBoardingNumber();
        String pathToFile = "src/main/java/com/example/boardingpassgui/BoardingPasses/" + boardingNumber;
        try {
            Files.createFile(Paths.get(pathToFile));
        } catch (IOException e) {
            // file already exists and is going to get overwritten
            // but the boarding numbers are guaranteed to be unique so the file should be already exist
            // and irl, once the passenger checks in, their boarding number could be removed from the set
        }
        StringBuilder sb = new StringBuilder("BOARDING NO: ").append(boardingNumber).append("\n\nPASSENGER INFO")
                .append("\n\tName: ").append(clientName).append("\n\tAge: ").append(clientAge)
                .append("\n\tGender: ").append(clientGender)
                .append("\n\tEmail: ").append(clientEmail).append("\n\tPhone Number: ")
                .append(clientPhoneNumber).append("\n\nTRIP DETAILS")
                .append("\n\tFlying from: ").append(cityOfDeparture.getDetailedName()).append(" - ").append(cityOfDeparture.getIataCode())
                .append("\n\tFlying to: ").append(cityOfArrival.getDetailedName()).append(" - ").append(cityOfArrival.getIataCode())
                .append("\n\tOn: ").append(departureDate)
                .append("\n\n\tTrip Cost: ").append(clientTotalCost).append("\n\n\tItinerary:");
        String totalTripDuration = null;
        
        for(FlightOfferSearch.Itinerary itinerary: clientFlight.getItineraries()) { // only one itinerary
            totalTripDuration = itinerary.getDuration(); 
            for(FlightOfferSearch.SearchSegment segment: itinerary.getSegments()) { // with at least 1 segment
                FlightOfferSearch.AirportInfo from = segment.getDeparture();
                FlightOfferSearch.AirportInfo to = segment.getArrival();
                String legDuration = segment.getDuration();
                sb.append("\n\t\tDeparting: ").append(from.getIataCode()).append(" -- ")
                        .append(from.getAt()).append(" -- Terminal ").append(from.getTerminal())
                        .append("\n\t\tArriving: ").append(to.getIataCode()).append(" -- ")
                        .append(to.getAt()).append(" -- Terminal ").append(to.getIataCode())
                        .append("\n\t\tTrip Duration: ").append(legDuration).append("\n");
            }
        }
        sb.append("\n\t\tTotal Trip Duration: ").append(totalTripDuration);
        // format and write boarding number along with all client info to file
        Files.writeString(Paths.get(pathToFile), sb.toString());
    }

    public String getBoardingPass() {
        try {
            return FileUtils.readFileToString(new File("src/main/java/com/example/boardingpassgui/BoardingPasses/" + boardingNumber), "utf-8");
        } catch (IOException e) {
            return e.toString();
        }
    }
}
