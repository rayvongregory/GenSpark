import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        printFirstScript();
        Random random = new Random();
        int friendlyDragon = random.nextInt(2)+1;
        Scanner scanner = new Scanner(System.in);

        int choice;
        while(true) {
            try {
                choice = scanner.nextInt();
                if(!isValid(choice)) throw new IllegalArgumentException("Choice must either be 1 or 2");
                break;
            } catch(InputMismatchException e) {
                println("");
                println("Input must be an integer.");
                print("Choice: ");
                scanner.nextLine();
            } catch(IllegalArgumentException e) {
                println(e.getMessage()+"\n");
                print("Choice: ");
            }
        }
        printSecondScript(friendlyDragon==choice);
    }

    public static void println(String string) {
        System.out.println(string);
    }

    public static boolean isValid(int input) {
        return input == 1 || input == 2;
    }

    public static void print(String string) {
        System.out.print(string);
    }

    public static void printFirstScript() {
        println("You are in a land full of dragons. In front of you,");
        println("you see two caves. In one cave, the dragon is friendly");
        println("and will share his treasure with you. The other dragon");
        println("is greedy and hungry and will eat you on sight.");
        println("Which cave will you go into? (1 or 2)");
        print("Choice: ");
    }

    public static void printSecondScript(boolean safe) {
        println("You approach the cave...");
        println("It is dark and spooky...");
        if(safe) {
            println("A large dragon jumps out in front of you! He opens his wallet and...");
            println("Shares his treasure with you!");
        } else {
            println("A large dragon jumps out in front of you! He opens his jaws and...");
            println("Gobbles you down in one bite!");
        }
    }
}
