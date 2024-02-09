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
 * Unchanged ( x )  :=  ( forall i in 0 until x : a[i] == a[i]' )
 */
@SuppressWarnings("DuplicatedCode")
public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int head;
    private int size;

    /**
     * Precondition:
     * element != null && queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n + 1 && a[n] == element
     */
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        assert element != null;
        ensureCapacity(queue);
        queue.elements[cyclicIndex(queue, queue.head + queue.size)] = element;
        queue.size++;
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        if (queue.size == queue.elements.length) {
            final int n = queue.elements.length;
            final Object[] newElements = new Object[n * 2];
            System.arraycopy(queue.elements, queue.head, newElements, 0, n - queue.head);
            System.arraycopy(queue.elements, 0, newElements, n - queue.head, queue.head);
            queue.head = 0;
            queue.elements = newElements;
        }
    }

    /**
     * Precondition:
     * n > 0 && queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R = a[0]
     */
    public static Object element(final ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    /**
     * Precondition:
     * n > 0 && queue != null;
     * <p>
     * Post-condition:
     * ( forall i in 0 until n' : a[i]' = a[i + 1] )  && n' == n - 1 && R = a[0]
     */
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        queue.size--;
        final Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = cyclicIndex(queue, queue.head + 1);
        return result;
    }

    /**
     * Precondition:
     * element != null && queue != null;
     * <p>
     * Post-condition:
     * ( forall i in 0 until n : a[i + 1]' = a[i] )  && n' == n + 1 && a[0] == element
     */
    public static void push(final ArrayQueueADT queue, final Object element) {
        assert queue != null;
        assert element != null;
        ensureCapacity(queue);
        queue.head = cyclicIndex(queue, queue.head - 1);
        queue.elements[queue.head] = element;
        queue.size++;
    }

    /**
     * Precondition:
     * n > 0 && queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == a[n - 1]
     */
    public static Object peek(final ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.elements[cyclicIndex(queue, queue.head + queue.size - 1)];
    }

    /**
     * Precondition:
     * n > 0 && queue != null;
     * <p>
     * Post-condition:
     * Unchanged ( n' )  && n' == n - 1 && R == a[n']
     */
    public static Object remove(final ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        queue.size--;
        final int index = cyclicIndex(queue, queue.head + queue.size);
        final Object res = queue.elements[index];
        queue.elements[index] = null;
        return res;
    }


    /**
     * Precondition:
     * queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == n
     */
    public static int size(final ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    /**
     * Precondition:
     * queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == (n == 0)
     */
    public static boolean isEmpty(final ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }

    /**
     * Precondition:
     * queue != null;
     * <p>
     * Post-condition:
     * n' == 0
     */
    public static void clear(final ArrayQueueADT queue) {
        assert queue != null;
        if (queue.head + queue.size > queue.elements.length) {
            Arrays.fill(queue.elements, queue.head, queue.elements.length, null);
            Arrays.fill(queue.elements, 0, queue.head + queue.size - queue.elements.length, null);
        } else {
            Arrays.fill(queue.elements, queue.head, queue.head + queue.size, null);
        }
        queue.size = 0;
    }

    /**
     * Precondition:
     * index in 0 until queue.size && queue != null;
     * <p>
     * Post-condition:
     * Unchanged(n) && n == n' && R == a[index]
     */
    public static Object get(final ArrayQueueADT queue, final int index) {
        assert queue != null;
        assert 0 <= index;
        assert index < queue.size;
        return queue.elements[cyclicIndex(queue, queue.head + index)];
    }

    /**
     * Precondition:
     * index in 0 until queue.size && element != null && queue != null;
     * <p>
     * Post-condition:
     * n == n' && element == a[index] &&  ( forall i in 0 until x : i != index => a[i] == a[i]' )
     */
    public static void set(final ArrayQueueADT queue, final int index, final Object element) {
        assert queue != null;
        assert 0 <= index;
        assert index < queue.size;
        assert element != null;
        queue.elements[cyclicIndex(queue, queue.head + index)] = element;
    }

    private static int cyclicIndex(final ArrayQueueADT queue, final int index) {
        return index < 0 ? index + queue.elements.length :
                index >= queue.elements.length ? index - queue.elements.length :
                        index;
    }

}
