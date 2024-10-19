import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByN = new OffByN(4);

    // Your tests go here.
    @Test
    public void testEqualChars() {
//        n = 5
//        assertTrue(offByN.equalChars('a', 'f'));
//        assertTrue(offByN.equalChars('f', 'a'));
//        assertFalse(offByN.equalChars('f', 'h'));
        // n = 1
//        assertTrue(offByN.equalChars('f', 'e'));
//        assertTrue(offByN.equalChars('l', 'k'));
//        assertFalse(offByN.equalChars('a', 'a'));
//        assertFalse(offByN.equalChars('a', 'z'));
//        assertTrue(offByN.equalChars('&', '%'));
        // n = 4
        assertTrue(offByN.equalChars('b', 'f'));
    }

}
