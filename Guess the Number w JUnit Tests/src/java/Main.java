import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        print("Hello! What is your name?");
        String name = scanner.nextLine();
        System.out.print("Well, " + name + " ");
        boolean stillPlaying = true;
        while(stillPlaying) {
            stillPlaying = play(scanner, name);
        }
        print("Thanks for playing, " + name+"!");
    }
    public static void print(String string) {
        System.out.println(string);
    }

    public static boolean isBetweenOneAnd20(int guess) {
        return guess > 0 && guess < 21;
    }

    public static boolean isGreaterThanCorrectAnswer(int guess, int correct) {
        return guess > correct;
    }

    public static boolean isLessThanCorrectAnswer(int guess, int correct) {
        return guess < correct;
    }

    public static boolean isTheCorrectAnswer(int guess, int correct) {
        return guess == correct;
    }

    public static boolean play(Scanner scanner, String name) {
        Random random = new Random();
        int correctAnswer = random.nextInt(20) + 1;
        int numberOfGuesses = 0;
        int guess = 0;
        print("I am thinking of a number between 1 and 20.");
        while (numberOfGuesses < 6) {
            print("Take a guess.");
            try {
                guess = scanner.nextInt();
                if (!isBetweenOneAnd20(guess)) throw new IllegalArgumentException("Guess must be between 1 and 20.");
                if (isLessThanCorrectAnswer(guess, correctAnswer)) print("Your guess is too low.");
                if (isGreaterThanCorrectAnswer(guess, correctAnswer)) print("Your guess is too high.");
            } catch (InputMismatchException e) {
                print("Guess must be an integer.");
            } catch (IllegalArgumentException e) {
                print(e.getMessage());
            }
            scanner.nextLine();
            numberOfGuesses++;
            if (isTheCorrectAnswer(guess, correctAnswer)) {
                print("Good job, " + name + "! You guessed my number in " + numberOfGuesses + " guesses!");
                print("Would you like to play again? (y or n)");
                String choice = scanner.nextLine();
                return !choice.equals("n");
            }

        }
        print("Sorry, " + name + ". You were not able to guess my number within six tries.");
        print("Would you like to play again? (y or n)");
        String choice = scanner.nextLine();
        return !choice.equals("n");
    }
}
