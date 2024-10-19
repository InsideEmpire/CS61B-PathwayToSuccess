import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('f', 'e'));
        assertTrue(offByOne.equalChars('e', 'f'));
        assertFalse(offByOne.equalChars('E', 'f'));
        assertFalse(offByOne.equalChars('F', 'e'));
        assertTrue(offByOne.equalChars('E', 'F'));
        assertTrue(offByOne.equalChars('l', 'k'));
        assertTrue(offByOne.equalChars('L', 'K'));
        assertFalse(offByOne.equalChars('a', 'a'));
        assertFalse(offByOne.equalChars('A', 'A'));
        assertFalse(offByOne.equalChars('a', 'z'));
        assertFalse(offByOne.equalChars('A', 'Z'));
        assertTrue(offByOne.equalChars('&', '%'));
    }
}
