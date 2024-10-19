public class LinkedListDeque<T> implements Deque<T> {
    private static class Node<T> {
        private Node<T> previous;
        private T item;
        private Node<T> next;
        public Node() {
            previous = null;
            item = null;
            next = null;
        }
        public Node(Node<T> p, T i, Node<T> n) {
            previous = p;
            item = i;
            next = n;
        }
        public Node(T i) {
            previous = null;
            item = i;
            next = null;
        }
        public Node(Node<T> p, T i) {
            previous = p;
            item = i;
            next = null;
        }
        public Node(T i, Node<T> n) {
            previous = null;
            item = i;
            next = n;
        }
    }

    private Node<T> headSentinel;
    private Node<T> endSentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        headSentinel = null;
        endSentinel = null;
    }

    //Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        headSentinel = new Node<>(item, headSentinel);
        if (size > 0) {
            headSentinel.next.previous = headSentinel;
        } else {
            endSentinel = headSentinel;
        }
        size++;
    }

    //Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        endSentinel = new Node<>(endSentinel, item);
        if (size > 0) {
            endSentinel.previous.next = endSentinel;
        } else {
            headSentinel = endSentinel;
        }
        size++;
    }

    //Returns true if deque is empty, false otherwise.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //Returns the number of items in the deque.
    @Override
    public int size() {
        return size;
    }

    //Prints the items in the deque from first to last, separated by a space.
    @Override
    public void printDeque() {
        Node<T> ptr = headSentinel;
        while (ptr != null) {
            System.out.print(ptr.item);
            System.out.print(" ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    //Removes and returns the item at the front of the deque. If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if (headSentinel != null) {
            Node<T> ptr = headSentinel;
            if (size != 1) {
                headSentinel = headSentinel.next;
                headSentinel.previous = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }
            T item = ptr.item;
            ptr.next = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }

    //Removes and returns the item at the back of the deque. If no such item exists, returns null.
    @Override
    public T removeLast() {
        if (endSentinel != null) {
            Node<T> ptr = endSentinel;
            if (size != 1) {
                endSentinel = endSentinel.previous;
                endSentinel.next = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }

            T item = ptr.item;
            ptr.previous = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }

    /*
    Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
    If no such item exists, returns null. Must not alter the deque!
    */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            int i = 0;
            Node<T> ptr = headSentinel;
            while (i != index) {
                i++;
                ptr = ptr.next;
            }
            return ptr.item;
        }
    }

    //Same as get, but uses recursion.
    // 递归实现的 get 方法
    public T getRecursive(int index) {
        // 如果索引无效，返回 null
        if (index < 0 || index >= size) {
            return null;
        }
        // 调用递归的辅助函数，从头节点开始
        return getRecursiveHelper(headSentinel, index);
    }

    // 辅助递归函数
    private T getRecursiveHelper(Node<T> ptr, int index) {
        // 基线条件：如果索引为 0，说明我们找到了目标节点
        if (index == 0) {
            return ptr.item;
        }
        // 否则递归查找下一个节点，同时索引减 1
        return (T) getRecursiveHelper(ptr.next, index - 1);
    }
}
