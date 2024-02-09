package expression.exceptions;

public class NegativeNumberPowerException extends EvaluationException {
    public NegativeNumberPowerException() {
    }

    public NegativeNumberPowerException(String s) {
        super(s);
    }

    public static NegativeNumberPowerException forOperand(Object left, Object value) {
        return new NegativeNumberPowerException(
                "Can't raise 10 to the power of %s = %s <= 0".formatted(left, value)
        );
    }
}
