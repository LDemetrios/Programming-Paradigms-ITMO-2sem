package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    @Override

    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    @Override
    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();

    @Override
    public void dropNth(int n) {
        removeNth(n); // Ignore result
    }
}
