package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;

    @Override
    public int capacity() {
        return capacity;
    }
    @Override
    public int fillCount() {
        return fillCount;
    }
    @Override
    public boolean isEmpty() {
        return fillCount() == 0;
    }
    @Override
    public boolean isFull() {
        return fillCount() == capacity();
    }

    public abstract T peek();
    public abstract T dequeue();
    public abstract void enqueue(T x);
}
