import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        buildTrieHelper(mapToMinPQ(frequencyTable));
    }

    private MinPQ<Node> mapToMinPQ(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> nodeMinPQ = new MinPQ<>();
        for (Character c : frequencyTable.keySet()) {
            Node node = new Node(c, frequencyTable.get(c));
            nodeMinPQ.insert(node);
        }
        return nodeMinPQ;
    }
    private Node buildTrieHelper(MinPQ<Node> nodeMinPQ) {
        while (nodeMinPQ.size() > 1) {
            Node node1 = nodeMinPQ.delMin();
            Node node2 = nodeMinPQ.delMin();
            Node newNode = new Node(node1, node2);
            nodeMinPQ.insert(newNode);
        }
        this.root = nodeMinPQ.min();

        return root;
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        int index = 0;
        Node node = root;
        StringBuilder bitSequence = new StringBuilder();

        while (!node.isLeaf()) {
            if (index >= querySequence.length()) {
                throw new IndexOutOfBoundsException("Query sequence is too short.");
            }

            if (querySequence.bitAt(index) == 0) {
                node = node.left;
                bitSequence.append('0');
            } else {
                node = node.right;
                bitSequence.append('1');
            }
            index++;
        }
        return new Match(new BitSequence(bitSequence.toString()), node.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildLookupTableHelper(root, new StringBuffer(), lookupTable);
        return lookupTable;
    }
    private void buildLookupTableHelper(Node node, StringBuffer bitSequence, Map<Character, BitSequence> lookupTable) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            lookupTable.put(node.ch, new BitSequence(bitSequence.toString()));
        } else {
            buildLookupTableHelper(node.left, bitSequence.append('0'), lookupTable);
            bitSequence.deleteCharAt(bitSequence.length() - 1);
            buildLookupTableHelper(node.right, bitSequence.append('1'), lookupTable);
            bitSequence.deleteCharAt(bitSequence.length() - 1);
        }
    }

    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = null;
            this.right = null;
        }

        Node(Node node1, Node node2) {
            this.ch = Character.MIN_VALUE;
            this.freq = node1.freq + node2.freq;
            this.left = node1.freq > node2.freq ? node2 : node1;
            this.right = node1.freq > node2.freq ? node1 : node2;
        }

        Node(int freq, Node left, Node right) {
            this.ch    = Character.MIN_VALUE;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
}
