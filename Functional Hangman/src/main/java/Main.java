import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    final static int MAX_NUM_GUESSES = 7;
    public static void main(String[] args) throws IOException {
        gameIntro();
        Player player = new Player(Player.getName());
        println(String.format("Nice to meet you, %s. Let's play!", player));
        playGame(player);
    }
    public static void playGame(Player player) throws IOException {
        Scanner userInput = new Scanner(System.in);
        player.setNumIncorrectGuesses(0);
        Word randomWord = new Word();
        String wordOrPhrase = "word";
        if(randomWord.isPhrase()) {
            randomWord.setCorrectLetters(" ");
            wordOrPhrase = "phrase";
        }
        printBoard(player.getNumIncorrectGuesses(), randomWord.getMissedLetters(), randomWord.getRemainingLetters());
        while(player.getNumIncorrectGuesses() < MAX_NUM_GUESSES) {
            print("Guess a letter: ");
            Guess guess = new Guess(userInput.nextLine().trim().toLowerCase()); // get user input
            if(guess.isEmpty()) {
                println("Huh? Did you say something?");
                continue;
            }
            boolean validGuess = guess.isValid(player.getNumIncorrectGuesses());
            if(!validGuess && !player.hasBeenWarned()) {
                println("That won't count against you this time but I'll add a body part next time.");
                player.hasNowBeenWarned();
                continue;
            }
            if(!validGuess && player.getNumIncorrectGuesses() == MAX_NUM_GUESSES-1) {
                println(String.format("Well, the %s was %s.", wordOrPhrase, randomWord));
                if(playAgain(userInput)) playGame(player);
                else exit(player);
            }
            if(!validGuess) {
                println("That's not a valid guess so I have to add a body part.");
                printBoard(player.incrementNumIncorrectGuesses(), randomWord.getMissedLetters(), randomWord.getRemainingLetters());
                continue;
            }
            if(randomWord.hasBeenGuessed(guess.toString())) {
                println("You already guessed that letter!");
                continue;
            }
            if(randomWord.contains(guess.toString())) {
                int charFrequency = randomWord.getCharacterFrequency(guess.toString());
                if(charFrequency > 1) println(String.format("Nice! The %s has %s %ss.", wordOrPhrase,charFrequency, guess.toUpperCase()));
                else println(String.format("Nice! The %s has 1 %s.", wordOrPhrase, guess.toUpperCase()));
                randomWord.setCorrectLetters(guess.toString());
                if(!randomWord.getRemainingLetters().contains("_")) {
                    if(player.getNumIncorrectGuesses() > MAX_NUM_GUESSES-2) print("Took ya long enough... ");
                    else print("Good job guessing all the letters! ");
                    println(String.format("The %s was %s.", wordOrPhrase, randomWord));
                    player.calculateScore(randomWord);
                    if(playAgain(userInput)) playGame(player);
                    else exit(player);
                }
                printBoard(player.getNumIncorrectGuesses(), randomWord.getMissedLetters(), randomWord.getRemainingLetters());
                println(String.format("Would you like to guess the %s? An incorrect guess won't count against you. (yes/no)", wordOrPhrase));
                if(yesOrNo(userInput).equals("yes")) {
                    println(String.format("Alright, what do you think the %s is?", wordOrPhrase));
                    String wOrP = userInput.nextLine();
                    if(randomWord.toString().equals(wOrP)) {
                        println(String.format("That's it! You got it! The %s was %s.", wordOrPhrase, randomWord));
                        player.calculateScore(randomWord);
                        if(playAgain(userInput)) playGame(player);
                        else exit(player);
                    } else {
                        println("Nope! Hahahaha!");
                    }
                }
            } else {
                println(String.format("There are no %ss in the %s.", guess.toUpperCase(), wordOrPhrase));
                randomWord.setMissedLetters(guess.toString());
                printBoard(player.incrementNumIncorrectGuesses(), randomWord.getMissedLetters(), randomWord.getRemainingLetters());
                if(player.getNumIncorrectGuesses() == MAX_NUM_GUESSES) {
                    println(String.format("Sorry, you lose. The %s was %s.", wordOrPhrase, randomWord));
                    if(playAgain(userInput)) playGame(player);
                    else exit(player);
                }
            }
        }
    }
    public static void println(String s) {
        System.out.println(s);
    }
    public static void print(String s) {
        System.out.print(s);
    }
    public static void gameIntro() throws IOException {
        println(Files.readString(Paths.get("src/main/java/Art/Introduction")));
    }
    public static void printBoard(int numberOfIncorrectGuesses, String missedLetters, String remainingLetters) throws IOException {
        println(Files.readString(Paths.get(String.format("src/main/java/Art/Case%s", numberOfIncorrectGuesses))));
        println("Missed letters: " + missedLetters);
        println(remainingLetters+"\n");
    }
    public static String yesOrNo(Scanner s) {
        String yesOrNo = s.nextLine();
        if(!yesOrNo.equals("yes") && !yesOrNo.equals("no")) {
            println("Sorry, please type 'yes' or 'no'.");
            return yesOrNo(s);
        }
        return yesOrNo;
    }
    public static boolean playAgain(Scanner s) {
        println("Would you like to play again? (yes/no)") ;
        return yesOrNo(s).equals("yes");
    }
    public static void exit(Player player) {
        println(String.format("Thanks for playing, %s!", player));
        System.exit(0);
    }
}
