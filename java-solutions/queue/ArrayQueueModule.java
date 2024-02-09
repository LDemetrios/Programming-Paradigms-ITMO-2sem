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
public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int head;
    private static int size;

    /**
     * Precondition:
     * element != null
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n + 1 && a[n] == element
     */
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity();
        elements[cyclicIndex(head + size)] = element;
        size++;
    }

    private static void ensureCapacity() {
        if (size == elements.length) {
            int n = elements.length;
            Object[] newElements = new Object[n * 2];
            System.arraycopy(elements, head, newElements, 0, n - head);
            System.arraycopy(elements, 0, newElements, n - head, head);
            head = 0;
            elements = newElements;
        }
    }

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R = a[0]
     */
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * (forall i in 0 until n' : a[i]' = a[i + 1]) && n' == n - 1 && R = a[0]
     */
    public static Object dequeue() {
        assert size > 0;
        size--;
        Object result = elements[head];
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
    public static void push(Object element) {
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
    public static Object peek() {
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
    public static Object remove() {
        assert size > 0;
        size--;
        int index = cyclicIndex(head + size);
        Object res = elements[index];
        elements[index] = null;
        return res;
    }


    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == n
     */
    public static int size() {
        return size;
    }

    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == (n == 0)
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * n' == 0
     */
    public static void clear() {
        if (head + size > elements.length) {
            Arrays.fill(elements, head, elements.length, null);
            Arrays.fill(elements, 0, head + size - elements.length, null);
        } else {
            Arrays.fill(elements, head, head + size, null);
        }
        size = 0;
    }

    /**
     * Precondition:
     * index in 0 until size
     * <p>
     * Post-condition:
     * Unchanged(n) && n == n' && R == a[index]
     */
    public static Object get(final int index) {
        assert 0 <= index;
        assert index < size;
        return elements[cyclicIndex(head + index)];
    }

    /**
     * Precondition:
     * index in 0 until size && element != null
     * <p>
     * Post-condition:
     * n == n' && element == a[index] && (forall i in 0 until x : i != index => a[i] == a[i]')
     */
    public static void set(final int index, Object element) {
        assert 0 <= index;
        assert index < size;
        assert element != null;
        elements[cyclicIndex(head + index)] = element;
    }

    private static int cyclicIndex(int index) {
        return index < 0 ? index + elements.length :
                index >= elements.length ? index - elements.length :
                        index;
    }

}
