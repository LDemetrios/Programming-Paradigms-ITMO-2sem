package expression.exceptions;

public class NotIntegerResultException extends EvaluationException {
    public NotIntegerResultException() {
    }

    public NotIntegerResultException(String s) {
        super(s);
    }

    public static NotIntegerResultException forBinary(Object left, Object symbol, Object right, Object leftValue, Object rightValue) {
        return new NotIntegerResultException(
                "The result of (%s) %s (%s) = (%s) %s (%s) is not an integer"
                        .formatted(left, symbol, right, leftValue, symbol, rightValue)
        );
    }

    public static NotIntegerResultException forUnary(Object symbol, Object value, Object operand) {
        return new NotIntegerResultException(
                "The result of %s(%s) = %s(%s) is not an integer"
                        .formatted(symbol, operand, symbol, value)
        );
    }
}
