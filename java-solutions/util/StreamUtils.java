package util;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public abstract class StreamUtils {
    public static <T> Stream<IndexedValue<T>> zipWithIndices(Stream<T> str) {
        AtomicInteger i = new AtomicInteger(0);
        return str.map(el -> new IndexedValue<>(el, i.getAndIncrement()));
    }

    public static <T> Consumer<T> rethrowing(ThrowingConsumer<T> parent) {
        return el -> {
            try {
                parent.accept(el);
            } catch (Throwable t) {
                sneakyThrow(t);
            }
        };
    }

    public static <E extends Throwable> void sneakyThrow(Throwable t) throws E {
        //noinspection unchecked
        throw (E) t;
    }

    public static <T> Collector<T, ?, IntList> toIntListCollector(ToIntFunction<T> func) {
        return new CollectorImpl<>(
                IntList::new,
                (a, t) -> a.addElement(func.applyAsInt(t)),
                (a, b) -> {
                    a.addAll(b);
                    return a;
                },
                Function.identity(),
                Set.of(Collector.Characteristics.IDENTITY_FINISH)
        );
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterator,
                        Spliterator.ORDERED
                ),
                false
        );
    }

    public static <T> Stream<T> stream(BooleanSupplier hasNext, Supplier<T> next) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<>() {
                            @Override
                            public boolean hasNext() {
                                return hasNext.getAsBoolean();
                            }

                            @Override
                            public T next() {
                                return next.get();
                            }
                        },
                        Spliterator.ORDERED
                ),
                false
        );
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Throwable;
    }
}
