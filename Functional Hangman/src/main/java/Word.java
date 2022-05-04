import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class Word {

    private String word;
    private ArrayList<String> arrayList;
    private final StringBuilder correctLetters = new StringBuilder();
    private final StringBuilder missedLetters = new StringBuilder();

    public Word () {
        this.word = getRandomWord();
        this.arrayList = new ArrayList<>(Arrays.asList(word.split("")));
    }

    public void setWord (String s) { // for testing
        this.word = s;
        this.arrayList = new ArrayList<>(Arrays.asList(word.split("")));
    }

    public static String getRandomWord() {
        Main.println("Give me a second to think of a random word or phrase...");
        String randomWord = RandomWordGenerator.getRandomWord().toLowerCase();
        Main.println("Alright, I've got it. Let's begin!\n");
        return randomWord;
    }

    public String toString() {
        return this.word;
    }

    public ArrayList<String> toList() {
        return this.arrayList;
    }

    public boolean isPhrase() {
        return this.word.trim().contains(" ");
    }

    public void setCorrectLetters(String s) {
        correctLetters.append(s);
    }

    public void setMissedLetters(String s) {
        missedLetters.append(s);
    }

    public String getMissedLetters() {
        return String.valueOf(missedLetters);
    }

    public String getRemainingLetters() {
        String str = Arrays.toString((this.toList().stream().map(l->{
            if(l.equals(" ")) return " ";
            if(this.hasBeenCorrectlyGuessed(l)) return l;
            return "_";
        }).toArray())).replaceAll(", ", "");
        return str.substring(1, str.length()-1);
    }

    public boolean hasBeenCorrectlyGuessed(String s) {
        return correctLetters.indexOf(s) > -1;
    }

    public boolean hasBeenGuessed(String s) {
        return hasBeenCorrectlyGuessed(s) || missedLetters.indexOf(s) > -1;
    }

    public boolean contains(String s) {
        return this.word.contains(s);
    }

    public int getCharacterFrequency(String s) {
        return (int) this.toList().stream().filter(c-> c.equals(s)).count();
    }
}
