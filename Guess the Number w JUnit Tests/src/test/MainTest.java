//import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void isGreaterThanCorrectAnswerTest() {
        Assertions.assertFalse(Main.isGreaterThanCorrectAnswer(20, 20));
        Assertions.assertFalse(Main.isGreaterThanCorrectAnswer(10, 10));
        Assertions.assertFalse(Main.isGreaterThanCorrectAnswer(9, 15));
        Assertions.assertFalse(Main.isGreaterThanCorrectAnswer(2, 5));
        Assertions.assertTrue(Main.isGreaterThanCorrectAnswer(7, 1));
        Assertions.assertTrue(Main.isGreaterThanCorrectAnswer(20, 19));
        Assertions.assertTrue(Main.isGreaterThanCorrectAnswer(18, 14));
        Assertions.assertTrue(Main.isGreaterThanCorrectAnswer(6, 3));
        Assertions.assertTrue(Main.isGreaterThanCorrectAnswer(12, 4));
    }

    @Test
    void isLessThanCorrectAnswerTest() {
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(20, 20));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(10, 10));
        Assertions.assertTrue(Main.isLessThanCorrectAnswer(9, 15));
        Assertions.assertTrue(Main.isLessThanCorrectAnswer(2, 5));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(7, 1));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(20, 19));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(18, 14));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(6, 3));
        Assertions.assertFalse(Main.isLessThanCorrectAnswer(12, 4));
    }

    @Test
    void isTheCorrectAnswerTest() {
        Assertions.assertTrue(Main.isTheCorrectAnswer(20, 20));
        Assertions.assertTrue(Main.isTheCorrectAnswer(10, 10));
        Assertions.assertTrue(Main.isTheCorrectAnswer(9, 9));
        Assertions.assertTrue(Main.isTheCorrectAnswer(5, 5));
        Assertions.assertFalse(Main.isTheCorrectAnswer(7, 1));
        Assertions.assertFalse(Main.isTheCorrectAnswer(20, 19));
        Assertions.assertFalse(Main.isTheCorrectAnswer(18, 14));
        Assertions.assertFalse(Main.isTheCorrectAnswer(6, 3));
        Assertions.assertFalse(Main.isTheCorrectAnswer(12, 4));
    }

}