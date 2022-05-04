public class Guess {

    private String val;

    public Guess(String val) {
        this.val = val;
    }

    public boolean isEmpty() {
        return this.val.length() == 0;
    }

    public boolean isValid(int numberIncorrectGuesses) {
        if(this.val.length() != 1) {
            if(numberIncorrectGuesses == Main.MAX_NUM_GUESSES-1) {
                Main.print("You used your final chance to guess that? Ugh... ");
                return false;
            }
            Main.print("You can only guess one letter at a time. ");
            return false;
        }
        if(!Character.isLetter(this.val.charAt(0))) {
            if(numberIncorrectGuesses == Main.MAX_NUM_GUESSES-1) {
                Main.print("You used your final chance to guess that? Ugh...");
                return false;
            }
            Main.print("You can only guess letters. ");
            return false;
        }
        return true;
    }

    public String toString() {
        return this.val;
    }

    public String toUpperCase() {
        return this.val.toUpperCase();
    }

}
