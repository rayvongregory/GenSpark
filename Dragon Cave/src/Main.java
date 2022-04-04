import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        printFirstScript();
        Random random = new Random();
        int friendlyDragon = random.nextInt(2)+1;
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        printSecondScript(friendlyDragon==choice);
    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static void printFirstScript() {
        print("You are in a land full of dragons. In front of you,");
        print("you see two caves. In one cave, the dragon is friendly");
        print("and will share his treasure with you. The other dragon");
        print("is greedy and hungry and will eat you on sight.");
        print("Which cave will you go into? (1 or 2)");
        System.out.print("Choice: ");
    }

    public static void printSecondScript(boolean safe) {
        print("You approach the cave...");
        print("It is dark and spooky...");
        if(safe) {
            print("A large dragon jumps out in front of you! He opens his wallet and...");
            print("Shares his treasure with you!");
       } else {
            print("A large dragon jumps out in front of you! He opens his jaws and...");
            print("Gobbles you down in one bite!");
       }
    }
}