import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("cac"));
        assertTrue(palindrome.isPalindrome("caaaac"));
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("c"));
        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("Aa"));
        assertFalse(palindrome.isPalindrome("caaaaaaaa"));
    }
    @Test
    public void testIsPalindromeInOffByOne() {
        assertTrue(palindrome.isPalindrome("flake", new OffByOne()));
        assertTrue(palindrome.isPalindrome("flke", new OffByOne()));
        assertTrue(palindrome.isPalindrome("FLaKE", new OffByOne()));
        assertTrue(palindrome.isPalindrome("fl&a%ke", new OffByOne()));
        assertFalse(palindrome.isPalindrome("aaaa", new OffByOne()));
        assertFalse(palindrome.isPalindrome("AAaA", new OffByOne()));
    }
    @Test
    public void testIsPalindromeInOffByN() {
        assertTrue(palindrome.isPalindrome("FLAKE", new OffByN(1)));
        assertTrue(palindrome.isPalindrome("flke", new OffByN(1)));
        assertTrue(palindrome.isPalindrome("fl&a%ke", new OffByN(1)));
        assertFalse(palindrome.isPalindrome("aaaa", new OffByN(1)));
        assertTrue(palindrome.isPalindrome("fFaHh", new OffByN(2)));
        assertTrue(palindrome.isPalindrome("fh", new OffByN(2)));
    }
}
