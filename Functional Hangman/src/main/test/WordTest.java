import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    Word word = new Word();

    @Test
    void isPhrase() {
        word.setWord("pie");
        assertFalse(word.isPhrase());
        word.setWord("pie crust");
        assertTrue(word.isPhrase());
        word.setWord("");
        assertFalse(word.isPhrase());
        word.setWord("              s ");
        assertFalse(word.isPhrase());
        word.setWord("                ");
        assertFalse(word.isPhrase());
    }

    @Test
    void getMissedLetters() {
        word.setWord("parkour");
        word.setMissedLetters("w");
        word.setMissedLetters("x");
        word.setMissedLetters("e");
        assertEquals("wxe", word.getMissedLetters());
        assertNotEquals("xew", word.getMissedLetters());
        word.setMissedLetters("l");
        assertEquals("wxel", word.getMissedLetters());
        assertNotEquals("xelw", word.getMissedLetters());
    }

    @Test
    void getRemainingLetters() {
        word.setWord("avocado");
        word.setCorrectLetters("o");
        word.setCorrectLetters("a");
        assertEquals("a_o_a_o", word.getRemainingLetters());
        word.setCorrectLetters("v");
        word.setCorrectLetters("d");
        assertEquals("avo_ado", word.getRemainingLetters());
        word.setCorrectLetters("c");
        assertNotEquals("avo_ado", word.getRemainingLetters());
        assertEquals("avocado", word.getRemainingLetters());
    }

    @Test
    void hasBeenCorrectlyGuessed() {
        word.setWord("giraffe");
        word.setCorrectLetters("f");
        assertTrue(word.hasBeenCorrectlyGuessed("f"));
        assertFalse(word.hasBeenCorrectlyGuessed("g"));
        word.setCorrectLetters("g");
        assertTrue(word.hasBeenCorrectlyGuessed("g"));
    }

    @Test
    void hasBeenGuessed() {
        word.setWord("desk");
        word.setMissedLetters("f");
        word.setMissedLetters("g");
        word.setMissedLetters("p");
        word.setCorrectLetters("d");
        word.setCorrectLetters("s");
        assertTrue(word.hasBeenGuessed("f"));
        assertTrue(word.hasBeenGuessed("g"));
        assertTrue(word.hasBeenGuessed("p"));
        assertTrue(word.hasBeenGuessed("d"));
        assertTrue(word.hasBeenGuessed("s"));
        assertFalse(word.hasBeenGuessed("x"));
        assertFalse(word.hasBeenGuessed("c"));
    }

    @Test
    void contains() {
        word.setWord("grapefruit");
        assertTrue(word.contains("p"));
        assertFalse(word.contains("w"));
        word.setWord("dragonfly");
        assertFalse(word.contains("p"));
        assertFalse(word.contains("w"));
    }

    @Test
    void getCharacterFrequency() {
        word.setWord("mississippi");
        assertEquals(4, word.getCharacterFrequency("i"));
        assertEquals(4, word.getCharacterFrequency("s"));
        assertEquals(2, word.getCharacterFrequency("p"));
        assertEquals(1, word.getCharacterFrequency("m"));
        assertEquals(0, word.getCharacterFrequency("x"));
        assertEquals(0, word.getCharacterFrequency("a"));
    }
}