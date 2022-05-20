import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void calculateScore() throws IOException {
        assertEquals(850, Main.calculateScore("Rayvon", "park","p",""));
        assertEquals(1050, Main.calculateScore("Rayvon", "military","m",""));
        assertEquals(1000, Main.calculateScore("Rayvon", "military","i",""));
        assertEquals(700, Main.calculateScore("Rayvon", "military","i","xnh"));
    }

    @Test
    void isHighScore() throws IOException { //based on the current high score of 650
        assertTrue(Main.isHighScore(700));
        assertFalse(Main.isHighScore(650));
    }

    @Test
    void getRemainingLetters() {
        assertEquals("a_o_a_o", Main.getRemainingLetters("avocado", "oa"));
        assertEquals("avo_ado", Main.getRemainingLetters("avocado", "oavd"));
        assertNotEquals("avo_ado", Main.getRemainingLetters("avocado", "oavdc"));
        assertEquals("avocado", Main.getRemainingLetters("avocado", "oavdc"));
    }
}