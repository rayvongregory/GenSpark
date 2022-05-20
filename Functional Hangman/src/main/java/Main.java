import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(Files.readString(Paths.get("src/main/java/Art/Introduction")));
        playGame(getPlayerName());
    }

    private static void playGame(String playerName) throws IOException {
        String randomWordOrPhrase = getRandomWordOrPhrase();
        nextRound(playerName, randomWordOrPhrase, randomWordOrPhrase.contains(" ") ? " " : "", "", false, new Scanner(System.in));
    }

    private static String getPlayerName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("What's your name?\nName: ");
        String name = scanner.nextLine();
        if (name.length() > 1) {
            System.out.printf("Nice to meet you, %s. Let's play!%n", name);
            return name;
        } else {
            System.out.println("Please enter a name with at least 2 characters.");
            return getPlayerName();
        }
    }

    private static String getRandomWordOrPhrase() {
        System.out.println("Give me a second to think of a random word or phrase...");
        String randomWord = RandomWordGenerator.getRandomWord().toLowerCase();
        System.out.println("Alright, I've got it. Let's begin!\n");
        return randomWord;
    }

    private static void nextRound(String playerName, String randomWordOrPhrase, String correctGuesses,
                                 String incorrectGuesses, boolean hasBeenWarned, Scanner userInput) throws IOException {
        if (incorrectGuesses.length() == 7) {
            System.out.printf("Sorry, you lose. The answer is %s.%n", randomWordOrPhrase);
            if (playAgain(userInput)) {
                playGame(playerName);
                return;
            } else exit(playerName);
        }
        int lettersLeftToGuess = (int) Arrays.stream(randomWordOrPhrase.split("")).filter(c -> !correctGuesses.contains(c)).count();
        if (lettersLeftToGuess == 0) {
            if (incorrectGuesses.length() > 5) System.out.print("Took ya long enough... ");
            else System.out.print("Good job guessing all the letters! ");
            System.out.printf("The answer was %s.%n", randomWordOrPhrase);
            saveScore(playerName, calculateScore(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses));
            if (playAgain(userInput)) {
                playGame(playerName);
                return;
            } else exit(playerName);
        }
        printBoard(correctGuesses, incorrectGuesses, randomWordOrPhrase);
        System.out.print("Guess a letter: ");
        String guess = userInput.nextLine().trim().toLowerCase(); // get user input
        if (guess.isEmpty()) {
            System.out.println("Huh? Did you say something?");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses, hasBeenWarned, userInput);
            return;
        }
        boolean guessOnlyOneCharacter = guess.length() == 1;
        if (!guessOnlyOneCharacter && !hasBeenWarned) {
            System.out.println("You can only guess one letter at a time! That won't count against you this time but I'll add a body part next time.");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses, true, userInput);
            return;
        }
        boolean guessIsALetter = Character.isLetter(guess.charAt(0));
        if (!guessIsALetter && !hasBeenWarned) {
            System.out.println("You can only guess letters! That won't count against you this time but I'll add a body part next time.");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses, true, userInput);
        } else if (!(guessOnlyOneCharacter && guessIsALetter) && incorrectGuesses.length() == 6) {
            System.out.print("You used your final chance to guess that? Ugh... ");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses + guess, true, userInput);
        } else if (!(guessOnlyOneCharacter && guessIsALetter)) {
            System.out.println("That's not a valid guess so I have to add a body part.");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses + guess, true, userInput);
        } else if ((correctGuesses + incorrectGuesses).contains(guess)) {
            System.out.println("You already guessed that letter!");
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses, hasBeenWarned, userInput);
        } else if (randomWordOrPhrase.contains(guess)) {
            int charFrequency = (int) Arrays.stream(randomWordOrPhrase.split("")).filter(c -> c.equals(guess)).count();
            if (charFrequency > 1)
                System.out.printf("Nice! The answer has %s %ss.%n", charFrequency, guess.toUpperCase());
            else System.out.printf("Nice! The answer has 1 %s.%n", guess.toUpperCase());
            printBoard(correctGuesses + guess, incorrectGuesses, randomWordOrPhrase);
            int numRemainingLetters = (int) Arrays.stream(randomWordOrPhrase.split("")).filter((correctGuesses + incorrectGuesses + guess)::contains).count();
            if (numRemainingLetters > 0) {
                System.out.print("Would you like to guess the answer? An incorrect guess won't count against you. (yes/no)\nResponse: ");
                if (yesOrNo(userInput).equals("yes")) {
                    System.out.print("Alright, what do you think the answer is?\nGuess: ");
                    if (randomWordOrPhrase.equals(userInput.nextLine())) {
                        System.out.printf("That's it! You got it! The answer was %s.%n", randomWordOrPhrase);
                        saveScore(playerName, calculateScore(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses));
                        if (playAgain(userInput)) {
                            playGame(playerName);
                            return;
                        } else exit(playerName);
                    } else {
                        System.out.println("Nope! Hahaha!");
                    }
                }
            }
            nextRound(playerName, randomWordOrPhrase, correctGuesses + guess, incorrectGuesses, hasBeenWarned, userInput);
        } else {
            System.out.printf("There are no %ss in the answer.", guess.toUpperCase());
            nextRound(playerName, randomWordOrPhrase, correctGuesses, incorrectGuesses + guess, hasBeenWarned, userInput);
        }
    }

    public static int calculateScore(String playerName, String randomWordOrPhrase, String correctGuesses, String incorrectGuesses) throws IOException {
        int playerScore = (7 - incorrectGuesses.length()) * 100 + ((int) Arrays.stream(randomWordOrPhrase.split("")).filter(c -> !correctGuesses.contains(c)).count() * 50);
        if (isHighScore(playerScore))
            System.out.printf("Congratulations, %s! You've set a new high score of %s points!", playerName, playerScore);
        else System.out.printf("You scored %s points this game.%n", playerScore);
        return playerScore;
    }

    private static void saveScore(String playerName, int playerScore) throws IOException {
        Files.writeString(Paths.get("src/main/resources/scores.txt"),
                String.format("%s,%s", playerName, playerScore) + System.lineSeparator(),
                StandardOpenOption.APPEND);
    }

    public static boolean isHighScore(int score) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/scores.txt"));
        for (String s : lines) {
            int readScore = Integer.parseInt(s.split(",")[1]);
            if (score <= readScore) return false;
        }
        return true;
    }

    private static void printBoard(String correctGuesses, String incorrectGuesses, String randomWord) throws IOException {
        System.out.println(Files.readString(Paths.get(String.format("src/main/java/Art/Case%s", incorrectGuesses.length()))));
        System.out.println("Missed letters: " + incorrectGuesses);
        System.out.println(getRemainingLetters(randomWord, correctGuesses) + "\n");
    }

    public static String getRemainingLetters(String randomWord, String correctGuesses) {
        String str = Arrays.toString((Arrays.stream(randomWord.split("")).map(l -> {
            if (l.equals(" ")) return " ";
            if (correctGuesses.contains(l)) return l;
            return "_";
        }).toArray())).replaceAll(", ", "");
        return str.substring(1, str.length() - 1);
    }

    private static String yesOrNo(Scanner s) {
        String yesOrNo = s.nextLine();
        if (!yesOrNo.equals("yes") && !yesOrNo.equals("no")) {
            System.out.print("Sorry, please type 'yes' or 'no'.\nResponse: ");
            return yesOrNo(s);
        }
        return yesOrNo;
    }

    private static boolean playAgain(Scanner s) {
        System.out.print("Would you like to play again? (yes/no)\nResponse: ");
        return yesOrNo(s).equals("yes");
    }

    private static void exit(String playerName) {
        System.out.printf("Thanks for playing, %s!%n", playerName);
        System.exit(0);
    }
}