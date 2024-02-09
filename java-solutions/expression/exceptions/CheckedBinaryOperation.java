package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperation;


public abstract class CheckedBinaryOperation extends BinaryOperation<TripleExpression> implements TripleExpression {
    public CheckedBinaryOperation(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    protected abstract int implementation(int left, int right);

    @Override
    public int evaluate(int x, int y, int z) {
        return implementation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    protected IntOverflowException overflow(int leftValue, int rightValue) {
        return IntOverflowException.forBinary(left.toMiniString(), operator().symbol(), right.toMiniString(), leftValue, rightValue);
    }

    protected NotIntegerResultException notAnInt(int leftValue, int rightValue) {
        return NotIntegerResultException.forBinary(left.toMiniString(), operator().symbol(), right.toMiniString(), leftValue, rightValue);
    }
}
