import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class Player {
    private final String name;
    private int score;
    private int numIncorrectGuesses = 0;
    private boolean beenWarned = false;
    private static final int MAX_NAME_LENGTH = 16;

    public Player(String name) {
        this.name = name;
    }

    public static String getName() {
        Scanner sc = new Scanner(System.in);
        Main.println("What's your name?");
        Main.print("Name: ");
        String name = sc.nextLine().trim();
        if(name.contains(",")) name.replaceAll(",", "" );
        if(name.length() <= 1) {
            Main.println("Did you say something? (All names must be at least 2 characters long.)");
            return getName();
        }
        if(name.length() >= MAX_NAME_LENGTH) {
            Main.println("Got a shorter name? (Pick a name that is less than 16 characters.)");
            return getName();
        }
        return name;
    }

    public String toString() {
        return this.name;
    }

    public boolean hasBeenWarned() {
        return this.beenWarned;
    }

    public void hasNowBeenWarned() {
        this.beenWarned = true;
    }

    public void setNumIncorrectGuesses(int num) {
        this.numIncorrectGuesses = num;
    }

    public int getNumIncorrectGuesses() {
        return this.numIncorrectGuesses;
    }

    public int incrementNumIncorrectGuesses() {
        return ++this.numIncorrectGuesses;
    }

    public int getScore() {
        return this.score;
    }

    public void calculateScore(Word randomWord) throws IOException {
        this.score = (Main.MAX_NUM_GUESSES - this.numIncorrectGuesses) * 100 +
                (int)(randomWord.getRemainingLetters().chars().filter(c->c=='_').count()) * 50;
        if(isHighScore(this.score)) Main.println(String.format("Congratulations, %s! You've set a new high score of %s points!", this.name, score));
        else Main.println(String.format("You scored %s points this game.", this.score));
        this.saveScore();
    }

    private void saveScore() throws IOException {
        Files.writeString(Paths.get("src/main/resources/scores.txt"),
                String.format("%s,%s", this.name, this.score)+System.lineSeparator(),
                StandardOpenOption.APPEND);
    }

    private boolean isHighScore(int score) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/scores.txt"));
        for(String s: lines) {
            int readScore = Integer.parseInt(s.split(",")[1]);
            if(score <= readScore) return false;
        }
        return true;
    }

}
