public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        boolean ifPalindrome = true;
        for (int i = 0; i < word.length(); i++) {
            if (i >= word.length() - i - 1) {
                break;
            }
            char forwardWord = word.charAt(i);
            char reverseWord = word.charAt(word.length() - i - 1);
            if (forwardWord != reverseWord) {
                ifPalindrome = false;
            }
        }
        return ifPalindrome;
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        boolean ifPalindrome = true;
        for (int i = 0; i < word.length(); i++) {
            if (i >= word.length() - i - 1) {
                break;
            }
            char forwardWord = word.charAt(i);
            char reverseWord = word.charAt(word.length() - i - 1);
            if (!cc.equalChars(forwardWord, reverseWord)) {
                ifPalindrome = false;
            }
        }
        return ifPalindrome;
    }
}
