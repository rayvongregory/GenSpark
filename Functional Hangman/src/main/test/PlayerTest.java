import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("Ray");
    Word word = new Word();

    @Test
    void hasBeenWarned() {
        assertFalse(player.hasBeenWarned());
        player.hasNowBeenWarned();
        assertTrue(player.hasBeenWarned());
    }

    @Test
    void incrementNumIncorrectGuesses() {
        assertEquals(1, player.incrementNumIncorrectGuesses());
        assertEquals(2, player.incrementNumIncorrectGuesses());
        assertEquals(3, player.incrementNumIncorrectGuesses());
        assertNotEquals(3, player.incrementNumIncorrectGuesses());
    }

    @Test
    void calculateScore() throws IOException {
        word.setWord("park");
        player.calculateScore(word);
        assertEquals(900, player.getScore());
        word.setWord("military");
        player.calculateScore(word);
        assertEquals(1100, player.getScore());
        player.incrementNumIncorrectGuesses();
        player.incrementNumIncorrectGuesses();
        player.incrementNumIncorrectGuesses();
        player.incrementNumIncorrectGuesses();
        player.incrementNumIncorrectGuesses();
        player.calculateScore(word);
        assertEquals(600, player.getScore());
        word.setCorrectLetters("i");
        player.calculateScore(word);
        assertEquals(500, player.getScore());

    }
}