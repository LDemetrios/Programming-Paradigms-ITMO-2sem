package expression.general;

public enum UnaryOperatorEnumImpl implements UnaryOperator {
    //Allows externally extending parser functionality with other operators
    NEGATION(3000, "-"),
    REVERSE(3000, "reverse"),
    POWER10(3000, "pow10"),
    LOGARITHM10(3000, "log10"),
    ;

    private final int priority;
    private final String symbol;

    UnaryOperatorEnumImpl(int priority, String symbol) {
        this.priority = priority;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "" + symbol;
    }

    public int priority() {
        return priority;
    }

    public String symbol() {
        return symbol;
    }
}
