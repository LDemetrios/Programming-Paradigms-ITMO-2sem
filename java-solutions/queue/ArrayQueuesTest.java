package queue;

import java.util.function.*;

import static util.Testing.*;

public class ArrayQueuesTest {

    public static void main(String[] args) {
        chapter("Test Module");

        testQueue(QueueMirror.ofModule());
        endChapter();

        chapter("ADT");
        QueueMirror instance1adt = QueueMirror.ofADT(new ArrayQueueADT());
        QueueMirror instance2adt = QueueMirror.ofADT(new ArrayQueueADT());
        checkIndependentQueues(instance1adt, instance2adt);

        endChapter();

        chapter("Object");
        QueueMirror instance1obj = QueueMirror.ofObject(new ArrayQueue());
        QueueMirror instance2obj = QueueMirror.ofObject(new ArrayQueue());
        checkIndependentQueues(instance1obj, instance2obj);
        endChapter();

        chapter("Linked");
        testQueue(QueueMirror.ofObject(new LinkedQueue()));
        endChapter();

        finish();
    }

    private static void checkIndependentQueues(QueueMirror instance1, QueueMirror instance2) {
        chapter("Instance 1");
        testQueue(instance1);
        endChapter();
        chapter("Instance 2");
        testQueue(instance2);
        endChapter();
        chapter("Independence");
        testIndependence(instance1, instance2);
        endChapter();
    }


    private static void testQueue(QueueMirror queue) {
        check("size", call(queue, "size"), 0);
        check("size doesn't affect queue", call(queue, "size"), 0);
        queue.enqueue("Obj1");
        queue.enqueue("Obj2");
        queue.enqueue("Obj3");
        check("size", call(queue, "size"), 3);
        check("dequeue", call(queue, "dequeue"), "Obj1");
        check("isEmpty", call(queue, "isEmpty"), false);
        check("element", call(queue, "element"), "Obj2");
        check("element doesn't affect queue", call(queue, "element"), "Obj2");
        check("dequeue", call(queue, "dequeue"), "Obj2");
        check("size", call(queue, "size"), 1);
        queue.clear();
        check("after cleaning", call(queue, "size"), 0);
        check("isEmpty", call(queue, "isEmpty"), true);
        queue.enqueue("Obj4");
        check("element", call(queue, "element"), "Obj4");
        check("size", call(queue, "size"), 1);
        check("dequeue", call(queue, "dequeue"), "Obj4");
        check("isEmpty", call(queue, "isEmpty"), true);
        queue.enqueue("Obj5");
        queue.enqueue("Obj6");
        check("peek", call(queue, "peek"), "Obj6");
        check("peek doesn't affect queue", call(queue, "peek"), "Obj6");
        check("remove", call(queue, "remove"), "Obj6");
        check("remove", call(queue, "remove"), "Obj5");
    }

    private static void testIndependence(QueueMirror queue1, QueueMirror queue2) {
        queue1.clear();
        queue2.clear();
        queue1.enqueue("Obj1");
        queue2.enqueue("Obj2");
        check("independence-1", queue2.dequeue(), "Obj2");
        check("independence-2", queue1.dequeue(), "Obj1");
    }


    public record QueueMirror(
            Consumer<Object> enqueueImpl,
            Supplier<Object> dequeueImpl,
            Supplier<Object> elementImpl,
            IntSupplier sizeImpl,
            BooleanSupplier isEmptyImpl,
            Runnable clearImpl,

            Consumer<Object> pushImpl,
            Supplier<Object> peekImpl,
            Supplier<Object> removeImpl,
            IntFunction<Object> getImpl,
            ObjIntConsumer<Object> setImpl
    ) {
        static QueueMirror ofADT(ArrayQueueADT instance) {
            return new QueueMirror(
                    el -> ArrayQueueADT.enqueue(instance, el),
                    () -> ArrayQueueADT.dequeue(instance),
                    () -> ArrayQueueADT.element(instance),
                    () -> ArrayQueueADT.size(instance),
                    () -> ArrayQueueADT.isEmpty(instance),
                    () -> ArrayQueueADT.clear(instance),
                    el -> ArrayQueueADT.push(instance, el),
                    () -> ArrayQueueADT.peek(instance),
                    () -> ArrayQueueADT.remove(instance),
                    ind -> ArrayQueueADT.get(instance, ind),
                    (el, ind) -> ArrayQueueADT.set(instance, ind, el)
            );
        }

        static QueueMirror ofObject(ArrayQueue instance) {
            return new QueueMirror(
                    instance::enqueue, instance::dequeue, instance::element,
                    instance::size, instance::isEmpty, instance::clear,
                    instance::push, instance::peek, instance::remove,
                    instance::get, (el, ind) -> instance.set(ind, el)
            );
        }

        static QueueMirror ofObject(LinkedQueue instance) {
            return new QueueMirror(
                    instance::enqueue, instance::dequeue, instance::element,
                    instance::size, instance::isEmpty, instance::clear,
                    null, null, null,
                    null, null
            );
        }

        static QueueMirror ofModule() {
            return new QueueMirror(
                    ArrayQueueModule::enqueue, ArrayQueueModule::dequeue, ArrayQueueModule::element,
                    ArrayQueueModule::size, ArrayQueueModule::isEmpty, ArrayQueueModule::clear,
                    ArrayQueueModule::push, ArrayQueueModule::peek, ArrayQueueModule::remove,
                    ArrayQueueModule::get, (el, ind) -> ArrayQueueModule.set(ind, el)
            );
        }

        public void enqueue(Object element) {
            enqueueImpl.accept(element);
        }

        public Object dequeue() {
            return dequeueImpl.get();
        }

        public Object element() {
            return elementImpl.get();
        }

        public int size() {
            return sizeImpl.getAsInt();
        }

        public boolean isEmpty() {
            return isEmptyImpl.getAsBoolean();
        }

        public void clear() {
            clearImpl.run();
        }

        public void push(Object element) {
            pushImpl.accept(element);
        }

        public Object peek() {
            return peekImpl.get();
        }

        public Object remove() {
            return removeImpl.get();
        }

        public Object get(int index) {
            return getImpl.apply(index);
        }

        public void set(int index, Object element) {
            setImpl.accept(element, index);
        }
    }
}
