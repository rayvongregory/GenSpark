import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessTest {
    Guess guess1 = new Guess("");
    Guess guess2 = new Guess("g");
    Guess guess3 = new Guess("1");
    Guess guess4 = new Guess("12");
    Guess guess5 = new Guess("aa");
    Guess guess6 = new Guess("a1");

    @Test
    // Do have to worry about trimming or converting to lowercase
    void isEmpty() {
        assertTrue(guess1.isEmpty());
        assertFalse(guess2.isEmpty());
        assertFalse(guess3.isEmpty());
        assertFalse(guess4.isEmpty());
        assertFalse(guess5.isEmpty());
        assertFalse(guess6.isEmpty());
    }

    @Test // arg doesn't matter
    void isValid() {
        assertFalse(guess1.isValid(1));
        assertTrue(guess2.isValid(1));
        assertFalse(guess3.isValid(1));
        assertFalse(guess4.isValid(1));
        assertFalse(guess5.isValid(1));
        assertFalse(guess6.isValid(1));
    }
}