package util;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two
 * {@code int}-valued arguments, and returns no result. This is the
 * {@code (int, int)} specialization of {@link BiConsumer}.
 * Unlike most other functional interfaces, {@code IntBiConsumer} is
 * expected to operate via side-effects.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #accept(int, int)}.
 *
 * @see BiConsumer
 * @since 1.8
 */
@FunctionalInterface
public interface IntBiConsumer {
    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(int t, int u);

    /**
     * Returns a composed {@code IntBiConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code IntBiConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default IntBiConsumer andThen(IntBiConsumer after) {
        Objects.requireNonNull(after);

        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
