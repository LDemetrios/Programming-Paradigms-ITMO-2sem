package expression.exceptions;

public class NonPositiveNumberLogarithmException extends EvaluationException {
    public NonPositiveNumberLogarithmException() {
    }

    public NonPositiveNumberLogarithmException(String s) {
        super(s);
    }

    public static NonPositiveNumberLogarithmException forOperand(Object left, Object value) {
        return new NonPositiveNumberLogarithmException(
                "Can't take logarithm of %s = %s <= 0".formatted(left, value)
        );
    }
}
