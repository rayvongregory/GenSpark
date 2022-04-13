import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        gameIntro();
        int numIncorrectGuesses;
        StringBuilder missedLetters;
        StringBuilder correctLetters;
        String guess;
        Scanner userInput = new Scanner(System.in);
        while(true) {
            numIncorrectGuesses = 0;
            String randomWord = getRandomWord();
            String remainingLetters = randomWord.replaceAll(".", "_");
            missedLetters = new StringBuilder();
            correctLetters = new StringBuilder();
            printBoard(numIncorrectGuesses, String.valueOf(missedLetters), remainingLetters);
            boolean beenWarned = false;
            while(numIncorrectGuesses < 8) {
                print("Guess a letter: ");
                guess = userInput.nextLine().trim(); // get user input
                if(guess.isEmpty()) {
                    println("Did you say something?");
                    continue;
                }
                boolean validGuess = isThisGuessValid(guess, numIncorrectGuesses);
                if(!validGuess && !beenWarned) {
                    println("That won't count against you this time but I'll add a body part next time.");
                    beenWarned = true;
                    continue;
                }
                if(!validGuess && numIncorrectGuesses == 7) {
                    println("Well, the word was " + randomWord);
                    if(playAgain(userInput)) break;
                    exit();
                }
                if(!validGuess) {
                    println("That's not a valid guess so I have to add a body part.");
                    printBoard(++numIncorrectGuesses, String.valueOf(missedLetters), remainingLetters) ;
                    continue;
                }
                if(letterInThisString(missedLetters+String.valueOf(correctLetters), guess)) {
                    println("You already guessed that letter!");
                    continue;
                }
                if(letterInThisString(randomWord, guess)) {
                    char char_ = guess.charAt(0);
                    int numberOfGuess = (int) randomWord.chars().filter(c-> c== char_).count();
                    if(numberOfGuess > 1) {
                        println("Nice! The word has " + numberOfGuess + " " + guess.toUpperCase() + "s.");
                    } else {
                        println("Nice! The word has 1 " + guess.toUpperCase() + ".");
                    }
                    correctLetters.append(guess);
                    remainingLetters = revealLetters(randomWord, guess, remainingLetters, numberOfGuess);
                    if(!remainingLetters.contains("_")) {
                        if(numIncorrectGuesses > 5) {
                            print("Took ya long enough... ");
                        } else {
                            print("Good job guessing all the letters! ");
                        }
                        println("The word was " + randomWord);
                        if (playAgain(userInput)) break;
                         exit();
                    }
                    printBoard(numIncorrectGuesses, String.valueOf(missedLetters), remainingLetters);
                    println("Would you like to guess the word? An incorrect guess won't count against you. (yes/no)");
                    guess = userInput.nextLine();
                    while(isNotValidYesOrNo(guess)) {
                        println("Sorry, please type 'yes' or 'no'.");
                        guess = userInput.nextLine();
                    }
                    if(guess.equals("yes")) {
                        println("Alright, what do you think the word is?");
                        guess = userInput.nextLine();
                        if(randomWord.equals(guess)) {
                            println("That's it! You got it! The word was " + randomWord);
                            if(playAgain(userInput)) break;
                            exit();
                        } else {
                            println("Nope! Hahahaha!");
                        }
                    }
                } else {
                    println("There are no " + guess.toUpperCase() + "s in the word.");
                    missedLetters.append(guess);
                    printBoard(++numIncorrectGuesses, String.valueOf(missedLetters), remainingLetters);
                    if(numIncorrectGuesses == 8) {
                        println("Sorry, you lose. The word was " + randomWord);
                        if(playAgain(userInput)) break;
                        System.exit(0);
                    }
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
    public static void gameIntro() {
        println("  +=====================+  ");
        println("  |    H A N G M A N    |  ");
        println("  +=====================+\n");
    }
    public static String getRandomWord() {
        println("Give me a second to think of a random word...");
        String randomWord = RandomWordGenerator.getRandomWord();
        println("Alright, I've got it. Let's begin!\n");
        return randomWord;
    }
    public static String revealLetters(String randomWord, String guess, String remainingLetters, int numberOfGuess) {
        int from = 0;
        for(int i = 0; i < numberOfGuess; i++) {
            int ind = randomWord.indexOf(guess, from);
            remainingLetters = remainingLetters.substring(0, ind) + guess + remainingLetters.substring(ind+1);
            from = ind+1;
        }
        return remainingLetters;
    }
    public static void printBoard(int numberOfIncorrectGuesses, String missedLetters, String remainingLetters) {
        println("          +-------+        ");
        switch (numberOfIncorrectGuesses) {
            case 0 -> {
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 1 -> {
                println("          O       |        ");
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 2 -> {
                println("          O       |        ");
                println("          |       |        ");
                println("                  |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 3 -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /        |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 4 -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /|       |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 5 -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /|\\      |        ");
                println("                  |        ");
                println("                  |        ");
            }
            case 6 -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /|\\      |        ");
                println("          |       |        ");
                println("                  |        ");
            }
            case 7  -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /|\\      |        ");
                println("          |       |        ");
                println("         /        |        ");
            }
            default -> {
                println("          O       |        ");
                println("          |       |        ");
                println("         /|\\      |        ");
                println("          |       |        ");
                println("         / \\      |        ");
            }
        }
        println("   ====================== ");
        println("Missed letters: " + missedLetters);
        println(remainingLetters+"\n");
    }

    public static boolean letterInThisString(String string, String letter) {
        return string.contains(letter);
    }

    public static boolean isOneChar(String userInput) {
        return userInput.length() == 1;
    }

    public static boolean isALetter(String s) {
            return Character.isLetter(s.charAt(0));
    }

    public static boolean isThisGuessValid(String guess, int numberIncorrectGuesses) {
        if(!isOneChar(guess)) {
            if(numberIncorrectGuesses == 7) {
                print("You used your final chance to guess to? Ugh...");
                return false;
            }
            print("You can only guess one letter at a time. ");
            return false;
        }
        if(!isALetter(guess)) {
            if(numberIncorrectGuesses == 7) {
                print("You used your final chance to guess to? Ugh...");
                return false;
            }
            print("You can only guess letters. ");
            return false;
        }
        return true;
    }

    public static boolean isNotValidYesOrNo(String userInput) {
        return !Objects.equals(userInput, "yes") && !Objects.equals(userInput, "no");
    }

    public static boolean playAgain(Scanner userInput) {
        println("Would you like to play again? (yes/no)") ;
        String response = userInput.nextLine();
        while(isNotValidYesOrNo(response)) {
            println("Sorry, please type 'yes' or 'no'.");
            response = userInput.nextLine();
        }
        return !response.equals("no");
    }
    public static void exit() {
        println("Thanks for playing!");
        System.exit(0);
    }
}
