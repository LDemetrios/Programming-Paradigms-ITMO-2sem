package expression.general;

import java.util.Objects;

public record UnaryOperatorRecordImpl(@Override int priority, @Override String symbol) implements UnaryOperator {
    public UnaryOperatorRecordImpl {
        Objects.requireNonNull(symbol);
    }
}
