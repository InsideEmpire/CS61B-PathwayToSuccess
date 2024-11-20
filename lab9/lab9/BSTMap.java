package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private static class Node<K, V> {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.equals(key)) {
            return (V) p.value;
        }
        if (key.compareTo((K) p.key) < 0) {
            return getHelper(key, p.left);
        }
        if (key.compareTo((K) p.key) > 0) {
            return getHelper(key, p.right);
        }
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, this.root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node<>(key, value);
        }
        // equal
        if (key.compareTo((K) p.key) == 0) {
            // already present, updates value
            p.value = value;
        }
        // greater
        if (key.compareTo((K) p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        }
        // less
        if (key.compareTo((K) p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySetHelper(root);
    }

    private Set<K> keySetHelper(Node p) {
        Set<K> set = new HashSet<>();
        if (p == null) {
            return set;
        }
        set.add((K) p.key);
        if (p.left != null) {
            set.addAll(keySetHelper(p.left));
        }
        if (p.right != null) {
            set.addAll(keySetHelper(p.right));
        }
        return set;
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        Node<K, V> removedNode = new Node<>(null, null); // To store the removed value
        root = removeHelper(root, key, removedNode);
        return removedNode.value;
    }

    private Node<K, V> removeHelper(Node<K, V> p, K key, Node<K, V> removedNode) {
        if (p == null) return null; // Key not found

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeHelper(p.left, key, removedNode); // Recur to the left subtree
        } else if (cmp > 0) {
            p.right = removeHelper(p.right, key, removedNode); // Recur to the right subtree
        } else {
            size--;
            // Node to be deleted is found
            removedNode.key = p.key; // Store removed key
            removedNode.value = p.value; // Store removed value

            if (p.left == null) {
                return p.right; // Replace with right child (handles case 1 and 2)
            } else if (p.right == null) {
                return p.left; // Replace with left child (handles case 1 and 2)
            } else {
                // Case 3: Node has two children
                Node<K, V> predecessor = getMax(p.left); // Find the in-order predecessor (left subtree's max)
                p.key = predecessor.key;
                p.value = predecessor.value;
                p.left = removeHelper(p.left, predecessor.key, new Node<>(null, null)); // Remove predecessor
            }
        }
        return p;
    }

    // Helper method to find the maximum node in a subtree
    private Node<K, V> getMax(Node<K, V> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        // Check if the key exists and is mapped to the specified value
        Node<K, V> dummy = new Node<>(null, null); // To store the removed node's value
        root = removeIfMatch(root, key, value, dummy);
        return dummy.value;
    }

    private Node<K, V> removeIfMatch(Node<K, V> p, K key, V value, Node<K, V> removedNode) {
        if (p == null) return null; // Key not found

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            // Recur to the left subtree
            p.left = removeIfMatch(p.left, key, value, removedNode);
        } else if (cmp > 0) {
            // Recur to the right subtree
            p.right = removeIfMatch(p.right, key, value, removedNode);
        } else {
            // Key matches; check if value matches
            if ((p.value == null && value != null) || (p.value != null && !p.value.equals(value))) {
                // Value does not match; no removal
                return p;
            }

            size--;
            // Value matches; perform removal
            removedNode.key = p.key;
            removedNode.value = p.value;

            if (p.left == null) {
                return p.right; // Replace with right child
            } else if (p.right == null) {
                return p.left; // Replace with left child
            } else {
                // Node has two children: replace with in-order predecessor
                Node<K, V> predecessor = getMax(p.left); // Find predecessor (left subtree's max)
                p.key = predecessor.key;
                p.value = predecessor.value;
                p.left = removeIfMatch(p.left, predecessor.key, predecessor.value, new Node<>(null, null));
            }
        }
        return p;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    private class BSTIterator implements Iterator<K> {
        private final Stack<Node<K, V>> stack;

        public BSTIterator() {
            stack = new Stack<>();
            pushLeft(root);
        }

        /** Push all left children of a node onto the stack. */
        private void pushLeft(Node<K, V> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Node<K, V> node = stack.pop();
            K key = node.key;
            // Push the leftmost path of the right child
            pushLeft(node.right);
            return key;
        }
    }
}
