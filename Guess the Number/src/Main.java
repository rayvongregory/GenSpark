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
    }
    public static void print(String string) {
        System.out.println(string);
    }

    public static boolean play(Scanner scanner, String name) {
        Random random = new Random();
        int correctAnswer = random.nextInt(21);
        int numberOfGuesses = 0;
        int guess;
        print("I am thinking of a number between 1 and 20.");
        while(numberOfGuesses < 6) {
            print("Take a guess.");
            guess = scanner.nextInt();
            scanner.nextLine();
            numberOfGuesses++;
            if(guess == correctAnswer) {
                print("Good job, " + name + "! You guessed my number in " + numberOfGuesses + " guesses!");
                print("Would you like to play again? (y or n)");
                String choice = scanner.nextLine();
                return !choice.equals("n");
            }
            if(guess < correctAnswer) print("Your guess is too low.");
            if(guess > correctAnswer) print("Your guess is too high.");
        }
        print("Sorry, " + name + ". You were not able to guess my number within six tries.");
        print("Would you like to play again? (y or n)");
        String choice = scanner.nextLine();
        return !choice.equals("n");
    }
}
