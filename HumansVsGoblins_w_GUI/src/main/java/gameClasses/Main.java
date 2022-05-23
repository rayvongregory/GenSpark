package gameClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner userInput = new Scanner(System.in);
        introduceGame(userInput);
        while(true) {
            Object[] landHumansGoblinsItems = configureGame(userInput);
            playGame(userInput, landHumansGoblinsItems);
        }
    }

    public static void introduceGame(Scanner userInput) {
        println("+=============================+");
        println("|      Humans vs Goblins      |");
        println("+=============================+\n");
        println("Play intro? (y/n)");
        print("Choice: ");
        String action;
        while(true) {
            action = userInput.nextLine();
            if(isValid(action, new String[]{"y", "n"})) break;
            println("Sorry, your input was not recognized. Try again. Enter y or n.");
        }
        if(action.equals("y")) gameIntro();
    }

    public static void gameIntro() {
        try {
            String content = new Scanner(new File("src/Java/IntroductionScript")).useDelimiter("\\Z").next();
            System.out.println(content);
        } catch (FileNotFoundException e) {
            println("File not found");
        }
    }

    public static Object[] configureGame(Scanner userInput) throws FileNotFoundException {
        String action;
        println("How many humans and goblins are on each team? (Choose a, b, or c.)\na) 3\nb) 5\nc) 7");
        print("Choice: ");
        while(true) {
            action = userInput.nextLine();
            if(isValid(action, new String[]{"a", "b", "c"})) break;
            println("Sorry, your input was not recognized. Try again. Enter a, b, or c.");
        }
        int numPlayers = getSize(action);
        Land land = new Land(numPlayers);
        int numRows = land.getNumRows();
        ArrayList<Human> humans = Human.generateHumans(numPlayers, numRows);
        ArrayList<Goblin> goblins = Goblin.generateGoblins(numPlayers, numRows);
        printPlayerStats(humans, goblins);
        ArrayList<Item> items = Item.generateItems(numPlayers, numRows, humans, goblins);
        return new Object[]{land, humans, goblins, items};
    }

    public static int getSize(String val) {
        if(val.equals("a")) return 3;
        if(val.equals("b")) return 5;
        return 7;
    }

    public static void printPlayerStats(ArrayList<Human> humans, ArrayList<Goblin> goblins) {
        println(String.format("\n%-30s %s" , "Meet your team...", "and the Goblins.\n"));
        for(int i = 0; i < humans.size(); i++) {
            Human h = humans.get(i);
            Goblin g = goblins.get(i);
            println(String.format("%-30s %s" , h.getIcon()+h.getName() + " (H%s)".formatted(h.getId()),
                    g.getIcon()+g.getName() + " (G%s)".formatted(g.getId())));
            println(String.format("%-30s %s" , "Damage: " + h.getDamage(), "Damage: " + g.getDamage()));
            println(String.format("%-30s %s" , "Defense: " + h.getDefense(), "Defense: " + g.getDefense()));
            println(String.format("%-30s %s" , "Health: " + h.getHealth(), "Health: " + g.getHealth()));
            println(String.format("%-30s %s" , "Mental Strength: " + h.getMentalStrength(),
                    "Aggression: " + g.getAggression()));
            println(String.format("%-30s %s\n" , "Position: " + Arrays.toString(h.getPosition()),
                    "Position: " + Arrays.toString(g.getPosition())));
        }
    }

    public static void playGame(Scanner userInput, Object[] lhgi) {
        Land land = (Land) lhgi[0];
        ArrayList<Human> humans = (ArrayList<Human>) lhgi[1];
        ArrayList<Goblin> goblins = (ArrayList<Goblin>) lhgi[2];
        ArrayList<Item> items = (ArrayList<Item>) lhgi[3];
        while (Human.notAllDead() && Goblin.notAllDead()) {
            Human h = humans.get(0);
            land.print(humans, goblins, items);
            println(h.toString());
            Object[] oc = h.getOptions(items); // get main menu when choice == 0, otherwise get submenu
            HashMap<String, String> options = (HashMap<String, String>) oc[0];
            ArrayList<String> choices = (ArrayList<String>) oc[1];
            while (true) {
                println("What would you like to do?");
                Human.printOptions(options);
                String action = userInput.nextLine();
                if (isValid(action, choices)) {
                    if (Integer.parseInt(action) == choices.size()) {
                        h.endTurn();
                        if (h.anotherHumanHere()) Human.healHumansHere(h.getPosition());
                        break;
                    }  // if the action is to end turn, then break out of the loop
                    h.handleChoice(Integer.parseInt(action), options, goblins, items);
                    if(h.getHealth() > 0) {
                        oc = h.getOptions(items);
                        options = (HashMap<String, String>) oc[0];
                        choices = (ArrayList<String>) oc[1];
                    } else {
                        System.out.printf("%s has died.%n", h.getName());
                        break;
                    }
                } else {
                    println("Sorry, your input was not recognized. Please try again. Press the number key associated with the action you want to perform");
                    Human.printOptions(oc[0]);
                }
            }
            if(Goblin.notAllDead()) Goblin.moveGoblin(humans, items);
            land.print(humans, goblins, items);
            if (!Human.notAllDead()) { // humans are all dead
                println("The Goblins have taken control of Earth...");
                println("Would you like to play again? (y/n)");
                print("Choice: ");
                String action;
                while(true) {
                    action = userInput.nextLine();
                    if(isValid(action, new String[]{"y", "n"})) break;
                    println("Sorry, your input was not recognized. Try again. Enter y or n.");
                }
                if(action.equals("y")) return;
                else System.exit(0);
            }
        }
        println("You've defeated all of the Goblins... now we rebuild.");
        println("Would you like to play again? (y/n)");
        print("Choice: ");
        String action;
        while(true) {
            action = userInput.nextLine();
            if(isValid(action, new String[]{"y", "n"})) break;
            println("Sorry, your input was not recognized. Try again. Enter y or n.");
        }
        if(action.equals("n")) System.exit(0);
    }

    public static boolean isValid(String input, String[] choices) {
        for(String s: choices) if(s.equals(input)) return true;
        return false;
    }

    public static boolean isValid(String input, Object choices) {
        return ((ArrayList<String>) choices).contains(input);
    }

    public static void print(String s) {System.out.print(s);}

    public static void println(String s) {System.out.println(s);}

}
