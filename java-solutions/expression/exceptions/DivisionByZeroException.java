package expression.exceptions;

public class DivisionByZeroException extends EvaluationException {
    public DivisionByZeroException() {
    }

    public DivisionByZeroException(String s) {
        super(s);
    }

    public static DivisionByZeroException forLeft(Object left) {
        return new DivisionByZeroException(
                "Can't divide %s by zero".formatted(left)
        );
    }

    public static DivisionByZeroException forBoth(Object left, Object right) {
        return new DivisionByZeroException(
                "Can't divide %s by %s, which is zero".formatted(left, right)
        );
    }
}
