package queue;

/**
 * Model:
 * n,
 * a[0]..a[n - 1]
 * <p>
 * Invariant:
 * (forall i in [0, n) : a[i] != null) && n >= 0
 * <p>
 * Notation:
 * Unchanged(x) := (forall i in [0, x) : a[i] == a[i]')
 */
public interface Queue {
    /**
     * Precondition:
     * element != null
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n + 1 && a[n] == element
     */
    void enqueue(Object element);

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R = a[0]
     */
    Object element();

    /**
     * Precondition:
     * n > 0
     * <p>
     * Post-condition:
     * (forall i in [0, n') : a[i]' = a[i + 1]) && n' == n - 1 && R = a[0]
     */
    Object dequeue();

    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == n
     */
    int size();

    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * Unchanged(n) && n' == n && R == (n == 0)
     */
    boolean isEmpty();

    /**
     * Precondition:
     * true
     * <p>
     * Post-condition:
     * n' == 0
     */
    void clear();


    /**
     * Unchanged(n) && n == n' && R.a[i] == this.a[i * k - 1]
     */
    Queue getNth(int k);

    /**
     * Precondition: true
     * <br>
     * Post-condition:<br>
     * forall i in [0, n') : a[i]' = a[i + floor(i / (k - 1))] && <br>
     * n' == n - floor(n / k) && <br>
     * R.n == floor(n / k) && <br>
     * R.a[i] == a[k * (i + 1) - 1] <br>
     */
    Queue removeNth(int k);

    /**
     * Precondition: true <br>
     * Post-condition: <br>
     * forall i in [0, n') : a[i]' = a[i + floor(i / (k - 1))] && <br>
     * n' == n - floor(n / k) && <br>
     */
    void dropNth(int k);
}
