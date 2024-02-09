package expression.exceptions;

public class IntOverflowException extends EvaluationException {
    public static final String EXCEED_INT_RANGE = "exceed the int range (-2147483648..2147483647)";

    public IntOverflowException() {
    }

    public IntOverflowException(String s) {
        super(s);
    }

    public static IntOverflowException forBinary(Object left, Object symbol, Object right, Object leftValue, Object rightValue) {
        return new IntOverflowException(
                "The result of (%s) %s (%s) = (%s) %s (%s) %s"
                        .formatted(left, symbol, right, leftValue, symbol, rightValue, EXCEED_INT_RANGE)
        );
    }

    public static IntOverflowException forUnary(Object symbol, Object operand, Object value) {
        return new IntOverflowException(
                "The result of %s(%s) = %s(%s) %s"
                        .formatted(symbol, operand, symbol, value, EXCEED_INT_RANGE)
        );
    }
}
