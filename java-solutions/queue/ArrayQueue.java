package queue;

import java.util.Arrays;

/**
 * Model:
 * n,
 * a[0]..a[n - 1]
 * <p>
 * Invariant:
 * forall i in 0 until n : a[i] != null
 * <p>
 * Notation:
 * Unchanged(x) := (forall i in 0 until x : a[i] == a[i]')
 */
@SuppressWarnings({"DuplicatedCode"})
public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int head;

    public ArrayQueue() {
        this(2);
    }

    private ArrayQueue(final int initialCapacity) {
        elements = new Object[initialCapacity];
    }

    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapacity();
        elements[cyclicIndex(head + size)] = element;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            final int n = elements.length;
            final Object[] newElements = new Object[n * 2];
            System.arraycopy(elements, head, newElements, 0, n - head);
            System.arraycopy(elements, 0, newElements, n - head, head);
            head = 0;
            elements = newElements;
        }
    }


    @Override
    protected Object elementImpl() {
        return elements[head];
    }


    @Override
    protected Object dequeueImpl() {
        final Object result = elements[head];
        elements[head] = null;
        head = cyclicIndex(head + 1);
        return result;
    }

    /**
     * Precondition:
     * element != null
     * <p>
     * Post-condition:
     * (forall i in 0 until n : a[i + 1]' = a[i]) && n' == n + 1 && a[0] == element
     */
    public void push(final Object element) {
        assert element != null;
        ensureCapacity();
        head = cyclicIndex(head - 1);
        elements[head] = element;
        size++;
    }

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == a[n - 1]
     */
    public Object peek() {
        assert size > 0;
        return elements[cyclicIndex(head + size - 1)];
    }

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * Unchanged(n') && n' == n - 1 && R == a[n']
     */
    public Object remove() {
        assert size > 0;
        size--;
        final int index = cyclicIndex(head + size);
        final Object res = elements[index];
        elements[index] = null;
        return res;
    }


    @Override
    protected void clearImpl() {
        if (head + size > elements.length) {
            Arrays.fill(elements, head, elements.length, null);
            Arrays.fill(elements, 0, head + size - elements.length, null);
        } else {
            Arrays.fill(elements, head, head + size, null);
        }
    }

    /**
     * Precondition:
     * index in [0, size)
     * <p>
     * Post-condition:
     * Unchanged(n) && n == n' && R == a[index]
     */
    public Object get(final int index) {
        assert 0 <= index && index < size;
        return elements[cyclicIndex(head + index)];
    }

    /**
     * Precondition:
     * index in 0 until size && element != null
     * <p>
     * Post-condition:
     * n == n' && element == a[index] && (forall i in 0 until x : i != index => a[i] == a[i]')
     */
    public void set(final int index, final Object element) {
        assert 0 <= index && index < size && element != null;
        elements[cyclicIndex(head + index)] = element;
    }

    private int cyclicIndex(final int index) {
        return index < 0 ? index + elements.length :
                index >= elements.length ? index - elements.length :
                        index;
    }

    @Override
    public Queue getNth(final int n) {
        final Queue result = new ArrayQueue(size / n + 1);
        for (int i = n - 1; i < size; i += n) {
            result.enqueue(get(i));
        }
        return result;
    }

    @Override
    public Queue removeNth(final int n) {
        final Queue result = new ArrayQueue(size / n + 1);
        int shift = 0;
        for (int i = n - 1; i < size; i++) {
            if ((i + 1) % n == 0) {
                result.enqueue(get(i));
                shift++;
            } else {
                set(i - shift, get(i));
            }
        }
        size -= shift;
        return result;
    }

    public static void main(final String[] args) {

    }
}
