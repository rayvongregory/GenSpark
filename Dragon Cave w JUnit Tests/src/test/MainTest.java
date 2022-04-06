import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MainTest {

    @Test
    void isValid() {
        Assertions.assertTrue(Main.isValid(1));
        Assertions.assertTrue(Main.isValid(2));
        Assertions.assertFalse(Main.isValid(0));
        Assertions.assertFalse(Main.isValid(-1));
        Assertions.assertFalse(Main.isValid(3));
    }
}