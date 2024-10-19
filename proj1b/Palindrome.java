
public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    // 使用数组解决
    //    public boolean isPalindrome(String word) {
    //        int len = word.length();
    //        int i = 0;
    //        int j = len - 1;
    //        for (; i < len / 2; i++, j--) {
    //            if (word.charAt(i) != word.charAt(j)) {
    //                return false;
    //            }
    //        }
    //        return true;
    //    }
    // 使用Deque解决
    public boolean isPalindrome(String word) {
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        int len = deque.size();
        int i = 0;
        int j = len - 1;
        for (; i < len / 2; i++, j--) {
            if (deque.get(i) != deque.get(j)) {
                return false;
            }
        }
        return true;
    }
    // 使用数组解决
    //    public boolean isPalindrome(String word, CharacterComparator cc) {
    //        int len = word.length();
    //        int i = 0;
    //        int j = len - 1;
    //        for (; i < len / 2; i++, j--) {
    //            if (!cc.equalChars(word.charAt(i), word.charAt(j))) {
    //                return false;
    //            }
    //        }
    //        return true;
    //    }
    // 使用Deque解决
    public boolean isPalindrome(String word, CharacterComparator cc) {
        LinkedListDeque<Character> deque = (LinkedListDeque<Character>) wordToDeque(word);
        int len = deque.size();
        int i = 0;
        int j = len - 1;
        for (; i < len / 2; i++, j--) {
            if (!cc.equalChars(deque.get(i), deque.get(j))) {
                return false;
            }
        }
        return true;
    }
}
