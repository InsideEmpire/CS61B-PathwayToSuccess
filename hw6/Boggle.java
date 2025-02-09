import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Boggle {

    public static void main(String[] args) {
        // 指定 Boggle 棋盘文件路径（请替换为你的实际路径）
        String boardFilePath = "exampleBoard.txt";
        int k = 7; // 取前 k 个最长的单词


        // 运行 Boggle 求解器
        List<String> result = Boggle.solve(k, boardFilePath);

        // 打印结果
        System.out.println("Found words:");
        for (String word : result) {
            System.out.println(word);
        }
    }

    // File path of dictionary file
    static String dictPath = "words.txt";
    static Trie trie;
    static char[][] wordGrid;
    static int totalColumn;
    static int totalRow;
    static Set<String> wordSet;

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        createWordGrid(boardFilePath);
        createTrie();
        wordSet = new TreeSet<>((a, b) -> {
            if (a.length() != b.length()) {
                return Integer.compare(b.length(), a.length()); // 按长度降序
            }
            return a.compareTo(b); // 按字典序升序
        });

        boolean[][] visited = new boolean[totalRow][totalColumn];

        for (int row = 0; row < totalRow; row++) {
            for (int column = 0; column < totalColumn; column++) {
                match(row, column, trie.root, new StringBuffer(), visited);
            }
        }

        return prune(k);
    }

    private static List<String> prune(int k) {
        List<String> wordList = new ArrayList<>(wordSet); // 直接转换为 List
        return wordList.size() <= k ? wordList : wordList.subList(0, k);
    }

    private static void match(int row, int column, Trie.Node current, StringBuffer word, boolean[][] visited) {
        if (current.isWord) {
            wordSet.add(word.toString());
        }
        if (current.next.isEmpty()) {
            return;
        }

        if (current != trie.root) {
            visited[row][column] = true; // 标记当前字母已访问
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int newRow = row + x;
                int newColumn = column + y;
//                if (newRow < 0) newRow = totalRow - 1;
//                else if (newRow >= totalRow) newRow = 0;
//                if (newColumn < 0) newColumn = totalColumn - 1;
//                else if (newColumn >= totalColumn) newColumn = 0;
                if (newRow < 0) newRow = 0;
                else if (newRow >= totalRow) newRow = totalRow - 1;
                if (newColumn < 0) newColumn = 0;
                else if (newColumn >= totalColumn) newColumn = totalColumn - 1;

                if (!visited[newRow][newColumn] && current.next.containsKey(wordGrid[newRow][newColumn])) {
                    word.append(wordGrid[newRow][newColumn]);
                    match(newRow, newColumn, current.next.get(wordGrid[newRow][newColumn]), word, visited);
                    word.deleteCharAt(word.length() - 1);
                }
            }
        }

        visited[row][column] = false; // 递归返回时取消访问标记
    }


    private static void createTrie() {
        In dictIn = new In(dictPath);
        trie = new Trie();
        while (dictIn.hasNextLine()) {
            String word = dictIn.readLine();
            trie.addWord(word);
        }
    }

    private static void createWordGrid(String boardFilePath) {
        In boardFileIn = new In(boardFilePath);
        String[] words = boardFileIn.readAllStrings();
        totalRow = words.length;
        totalColumn = words[0].length();
        wordGrid = new char[totalRow][totalColumn];
        for (int row = 0; row < totalRow; row++) {
            for (int column = 0; column < totalColumn; column++) {
                wordGrid[row][column] = words[row].charAt(column);
            }
        }
    }
}
