public class ArrayDeque<T> implements Deque<T> {
    private T[] array;
    private int size;
    private int frontPtr;
    private int backPtr;
    private int capacity;
    public ArrayDeque() {
        array = (T[]) new Object[8];
        capacity = 8;
        frontPtr = 0; // the first item
        backPtr = 0; // one index after the last item
        size = 0;
    }

    private boolean isFull() {
        return (backPtr) % capacity == frontPtr && size == capacity;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    private void update() {
        if (isFull()) {
            // 扩容逻辑
            resize(capacity * 2);  // 扩展为当前容量的两倍
        } else if (size > 0 && size * 4 < capacity && capacity >= 16) {
            // 缩小容量逻辑
            resize((Math.max(capacity / 2, 16)));  // 缩小为当前容量的一半，但不低于最小容量
        }
    }

    private void resize(int newCapacity) {
        T[] newArray = (T[]) new Object[newCapacity];
        int index = frontPtr;
        for (int i = 0; i < size; i++) {
            newArray[i] = array[index];
            index = (index + 1) % capacity;
        }
        frontPtr = 0;
        backPtr = size;  // backPtr 是 size 之后的位置
        array = newArray;
        capacity = newCapacity;
    }

    @Override
    public void addFirst(T item) {
        update();
        frontPtr = (frontPtr - 1 + capacity) % capacity;
        array[frontPtr] = item;
        size++;
    }
    @Override
    public void addLast(T item) {
        update();
        array[backPtr] = item;
        backPtr = (backPtr + 1) % capacity;
        size++;
    }

    @Override
    public int size() {
        if (size < 0) {
            return 0;
        } else {
            return size;
        }
    }

    @Override
    public void printDeque() {
        int index = frontPtr;
        while (index != backPtr) {
            System.out.print(array[index]);
            System.out.print(" ");
            index = (index + 1) % capacity;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        T item = array[frontPtr];
        frontPtr = (frontPtr + 1) % capacity;
        size--;
        update();
        return item;
    }
    @Override
    public T removeLast() {
        backPtr = (backPtr - 1 + capacity) % capacity;
        T item = array[backPtr];
        size--;
        update();
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null; // 或者抛出 IllegalArgumentException
        }
        int i = frontPtr;
        for (int j = 0; j < index; j++) {
            i = (i + 1) % capacity;
        }
        return array[i];
    }
}
