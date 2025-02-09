import java.util.HashMap;
import java.util.Map;

public class Trie {
    public Node root;

    Trie() {
        root = new Node('\0', false);
    }

    public void addWord(String aWord) {
        Node current = root;
        for (int i = 0; i < aWord.length(); i++) {
            addLetter(current, aWord.charAt(i), i == aWord.length() - 1);
            current = current.next.get(aWord.charAt(i));
        }
    }

    public void addLetter(Node current, char aChar, boolean isEnd) {
        if (current.next.containsKey(aChar)) {
            current.next.get(aChar).isWord |= isEnd;
        } else {
            current.next.put(aChar, new Node(aChar, isEnd));
        }
    }

    public boolean containsWord(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            if (!current.next.containsKey(c)) {
                return false;
            }
            current = current.next.get(c);
        }
        return current.isWord;
    }

    public boolean startsWith(String prefix) {
        Node current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.next.containsKey(c)) {
                return false;
            }
            current = current.next.get(c);
        }
        return true;
    }

    public boolean removeWord(String word) {
        return removeHelper(root, word, 0);
    }

    private boolean removeHelper(Node current, String word, int index) {
        if (index == word.length()) {
            if (!current.isWord) {
                return false;
            }
            current.isWord = false;
            return current.next.isEmpty();
        }

        char c = word.charAt(index);
        if (!current.next.containsKey(c)) {
            return false;
        }

        boolean shouldDeleteCurrentNode = removeHelper(current.next.get(c), word, index + 1);

        if (shouldDeleteCurrentNode) {
            current.next.remove(c);
            return current.next.isEmpty() && !current.isWord;
        }

        return false;
    }

    static public class Node {
        char aChar;
        Map<Character, Node> next;
        boolean isWord;

        Node(char aChar, boolean isWord) {
            this.aChar = aChar;
            this.next = new HashMap<>();
            this.isWord = isWord;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.addWord("hello");
        trie.addWord("hell");
        trie.addWord("he");

        System.out.println(trie.containsWord("hello")); // true
        System.out.println(trie.containsWord("hell"));  // true
        System.out.println(trie.containsWord("he"));    // true
        System.out.println(trie.containsWord("hero"));  // false
        System.out.println(trie.startsWith("he"));      // true
        System.out.println(trie.startsWith("ha"));      // false

        trie.removeWord("hello");
        System.out.println(trie.containsWord("hello")); // false
        System.out.println(trie.containsWord("hell"));  // true
    }
}
