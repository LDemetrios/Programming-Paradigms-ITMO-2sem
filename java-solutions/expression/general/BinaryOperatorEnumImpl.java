package expression.general;

import java.util.Objects;

//Suddenly, because `inverse` should exist, there is no way to make this class record
public enum BinaryOperatorEnumImpl implements BinaryOperator {
    //Allows externally extending parser functionality with other operators
    SUBTRACTION(1000, "-", false),
    ADDITION(1000, "+", true, SUBTRACTION),
    MULTIPLICATION(2000, "*", true),
    DIVISION(2000, "/", false),
    GCD(0, "gcd", true),
    LCM(0, "lcm", true),
    ;


    private final int priority;
    private final String symbol;
    private final boolean associative;
    private final BinaryOperator inverse;

    BinaryOperatorEnumImpl(int priority, String symbol, boolean associative) {
        this.priority = priority;
        this.symbol = Objects.requireNonNull(symbol);
        this.associative = associative;
        this.inverse = null;
    }

    BinaryOperatorEnumImpl(int priority, String symbol, boolean associative, BinaryOperator inverse) {
        this.priority = priority;
        this.symbol = Objects.requireNonNull(symbol);
        this.associative = associative;
        this.inverse = inverse == null ? this : inverse; // For example, XOR is inverse for itself
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
}
