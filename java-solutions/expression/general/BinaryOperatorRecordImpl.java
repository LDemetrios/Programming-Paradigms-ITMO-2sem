package expression.general;

import java.util.Objects;

//Suddenly, because `inverse` should exist, there is no way to make this class record
public class BinaryOperatorRecordImpl implements BinaryOperator {
    private final int priority;
    private final String symbol;
    private final boolean associative;
    private final BinaryOperator inverse;

    public BinaryOperatorRecordImpl(int priority, String symbol, boolean associative) {
        this.priority = priority;
        this.symbol = Objects.requireNonNull(symbol);
        this.associative = associative;
        this.inverse = null;
    }

    public BinaryOperatorRecordImpl(int priority, String symbol, boolean associative, BinaryOperator inverse) {
        this.priority = priority;
        this.symbol = Objects.requireNonNull(symbol);
        this.associative = associative;
        this.inverse = inverse == null ? this : inverse;
    }

    @Override
    public BinaryOperator inverse() {
        return inverse;
    }

    @Override
    public String toString() {
        return "" + symbol;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public boolean associative() {
        return associative;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BinaryOperatorRecordImpl) obj;
        return this.priority == that.priority &&
                Objects.equals(this.symbol, that.symbol) &&
                this.associative == that.associative;
    }

    @Override
    public int hashCode() {
        final int coeff = 31;
        return coeff * (coeff * priority + symbol.hashCode()) + (associative ? 1231 : 1237);
    }
}
