package lab9;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }
    private int hash(K key, int newSize) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), newSize);
    }

    private void resize() {
        int newSize = buckets.length * 2;
        ArrayMap<K, V>[] newBuckets = new ArrayMap[newSize];

        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = new ArrayMap<>();
        }

        for (int i = 0; i < buckets.length; i++) {
            Iterator<K> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                K key = iterator.next();
                newBuckets[hash(key, newSize)].put(key, buckets[i].get(key));
            }
        }
        buckets = newBuckets;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int bucketIndex = hash(key);
        return buckets[bucketIndex].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int bucketIndex = hash(key);

        if (!buckets[bucketIndex].containsKey(key)) {
            size++;
        }
        
        buckets[bucketIndex].put(key, value);
        if (loadFactor() > MAX_LF) {
            resize();
        }
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
        if (size == 0) {
            return null;
        }
        Set<K> set = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            set.addAll(buckets[i].keySet());
        }
        return set;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int bucketIndex = hash(key);
        return buckets[bucketIndex].remove(key);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int bucketIndex = hash(key);
        return buckets[bucketIndex].remove(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        int index = 0;
        Iterator<K> iterator = buckets[index].iterator();

        private boolean switchNextIndex() {
            while (!iterator.hasNext()) {
                index++;
                if (index == buckets.length) {
                    return false;
                }
                iterator = buckets[index].iterator();
            }
            return true;
        }

        @Override
        public boolean hasNext() {
            if (iterator.hasNext()) {
                return true;
            } else {
                return switchNextIndex();
            }
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return iterator.next();
        }
    }
}
