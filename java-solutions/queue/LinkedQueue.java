package queue;

public class LinkedQueue extends queue.AbstractQueue {


    private static class Node {
        private final Object element;
        private Node next;

        public Node(Object element, Node next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node head;
    private Node phantomHead = new Node(null, head);
    private Node tail = phantomHead;

    private void enqueueSilent(Object element) {
        assert element != null;
        Node newTail = new Node(element, null);
        tail.next = newTail;
        tail = newTail;
        if (head == null) {
            head = phantomHead.next;
        }
        size++;
    }

    @Override
    protected void enqueueImpl(Object element) {
        Node newTail = new Node(element, null);
        tail.next = newTail;
        tail = newTail;
        if (head == null) {
            head = phantomHead.next;
        }
    }


    @Override
    protected Object elementImpl() {
        return head.element;
    }


    @Override
    protected Object dequeueImpl() {
        Object res = head.element;
        phantomHead.next = head.next;
        head = head.next;
        if (head == null) {
            tail = phantomHead;
        }
        return res;
    }

    @Override
    protected void clearImpl() {
        head = null;
        phantomHead = new Node(null, null);
        tail = phantomHead;
    }

    // :NOTE: copy-paste
    @Override
    public LinkedQueue getNth(int n) {
        LinkedQueue result = new LinkedQueue();

        int index = 0;
        Node pointer = phantomHead;
        while (pointer.next != null) {
            index++;
            pointer = pointer.next;
            if (index % n == 0) {
                result.enqueueSilent(pointer.element);
            }
        }
        return result;
    }

    @Override
    public LinkedQueue removeNth(int n) {
        if (n == 1) {
            return migrate();
        }

        LinkedQueue result = new LinkedQueue();
        int index = 0;
        Node pointer = phantomHead;
        while (pointer.next != null) {
            if ((index + 1) % n == 0) {
                result.enqueueSilent(pointer.next.element);
                pointer.next = pointer.next.next;
                size--;
            } else {
                pointer = pointer.next;
            }
            index++;
        }
        tail = pointer;
        return result;
    }

    private LinkedQueue migrate() {
        LinkedQueue res = new LinkedQueue();
        res.phantomHead = this.phantomHead;
        res.head = this.head;
        res.tail = this.tail;
        res.size = this.size;
        this.clear();
        return res;
    }
}
