package expression.exceptions;

public class ExpressionFormatException extends IllegalArgumentException {
    public ExpressionFormatException() {
    }

    public ExpressionFormatException(String s) {
        super(s);
    }

    public ExpressionFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionFormatException(Throwable cause) {
        super(cause);
    }
}
