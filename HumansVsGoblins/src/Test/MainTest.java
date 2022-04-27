import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class MainTest {
    @Test
    void testIsValid1() {
        Assertions.assertTrue(Main.isValid("y", new String[] {"y", "n"}));
        Assertions.assertFalse(Main.isValid("k", new String[] {"y", "n"}));
        Assertions.assertFalse(Main.isValid("6", new String[] {"y", "n"}));
        Assertions.assertFalse(Main.isValid("p", new String[] {"y", "n"}));
        Assertions.assertFalse(Main.isValid("rayvon", new String[] {"y", "n"}));
        Assertions.assertTrue(Main.isValid("n", new String[] {"y", "n"}));
        Assertions.assertTrue(Main.isValid("313", new String[] {"y", "n", "313"}));
        Assertions.assertFalse(Main.isValid("\n", new String[] {"y", "n", "313"}));
    }

    @Test
    void testIsValid2() {
        Assertions.assertTrue(Main.isValid("o", new ArrayList<>(Arrays.asList("o","w", "513"))));
        Assertions.assertFalse(Main.isValid("p", new ArrayList<>(Arrays.asList("o","w", "513"))));
        Assertions.assertTrue(Main.isValid("rayvon", new ArrayList<>(Arrays.asList("rayvon","w", "513"))));
        Assertions.assertFalse(Main.isValid("gregory", new ArrayList<>(Arrays.asList("rayvon","w", "513"))));
        Assertions.assertFalse(Main.isValid("priceisright", new ArrayList<>(Arrays.asList("rayvon","w", "513"))));
    }
}