package expression.exceptions;

public class EvaluationException extends ArithmeticException {
    public EvaluationException() {
    }

    public EvaluationException(String s) {
        super(s);
    }
}
