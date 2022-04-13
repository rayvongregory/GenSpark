import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void revealLetters() {
        Assertions.assertEquals("_a__", Main.revealLetters("cart", "a", "____", 1));
        Assertions.assertNotEquals("___t", Main.revealLetters("cart", "a", "____", 1));
        Assertions.assertEquals("h___h", Main.revealLetters("hatch", "h", "_____", 2));
        Assertions.assertEquals("hat_h", Main.revealLetters("hatch", "a", "h_t_h", 1));
        Assertions.assertNotEquals("hat__", Main.revealLetters("hatch", "h", "_at__", 2));
        Assertions.assertEquals("__ss_ss____", Main.revealLetters("mississippi", "s", "___________", 4));
        Assertions.assertNotEquals("__ss_s_____", Main.revealLetters("mississippi", "s", "___________", 4));
        Assertions.assertNotEquals("___________", Main.revealLetters("mississippi", "s", "___________", 4));
        Assertions.assertEquals("_ississi__i", Main.revealLetters("mississippi", "i", "__ss_ss____", 4));
    }

    @Test
    void letterInThisString() {
        Assertions.assertTrue(Main.letterInThisString("randy", "r"));
        Assertions.assertTrue(Main.letterInThisString("randy", "y"));
        Assertions.assertFalse(Main.letterInThisString("randy", "i"));
        Assertions.assertTrue(Main.letterInThisString("randy", "d"));
    }

    @Test
    void isOneChar() {
        Assertions.assertTrue(Main.isOneChar("f"));
        Assertions.assertFalse(Main.isOneChar(""));
        Assertions.assertTrue(Main.isOneChar("."));
        Assertions.assertFalse(Main.isOneChar("wwe"));
    }

    @Test
    void isALetter() {
        Assertions.assertTrue(Main.isALetter("f"));
        Assertions.assertTrue(Main.isALetter("r"));
        Assertions.assertFalse(Main.isALetter(" "));
        Assertions.assertFalse(Main.isALetter("."));
        Assertions.assertFalse(Main.isALetter("9"));
    }

    @Test
    void isThisGuessValid() {
        Assertions.assertTrue(Main.isThisGuessValid("a",1));
        Assertions.assertTrue(Main.isThisGuessValid("e",1));
        Assertions.assertTrue(Main.isThisGuessValid("w",1));
        Assertions.assertFalse(Main.isThisGuessValid("is",1));
        Assertions.assertFalse(Main.isThisGuessValid("69",1));
        Assertions.assertFalse(Main.isThisGuessValid("2.0",1));
        Assertions.assertFalse(Main.isThisGuessValid("",1));
        Assertions.assertFalse(Main.isThisGuessValid(" ",1));
        Assertions.assertFalse(Main.isThisGuessValid(".",1));
        Assertions.assertFalse(Main.isThisGuessValid(" . ",1));
    }

    @Test
    void isNotValidYesOrNo() {
        Assertions.assertTrue(Main.isNotValidYesOrNo("y"));
        Assertions.assertTrue(Main.isNotValidYesOrNo("n"));
        Assertions.assertFalse(Main.isNotValidYesOrNo("yes"));
        Assertions.assertFalse(Main.isNotValidYesOrNo("no"));
    }

    @Test
    void playAgain() {
        Assertions.assertTrue(Main.isNotValidYesOrNo("y"));
        Assertions.assertTrue(Main.isNotValidYesOrNo("n"));
        Assertions.assertFalse(Main.isNotValidYesOrNo("yes"));
        Assertions.assertFalse(Main.isNotValidYesOrNo("no"));
    }
}