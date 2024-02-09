package expression.exceptions;

import expression.TripleExpression;
import expression.general.UnaryOperation;


public abstract class CheckedUnaryOperation extends UnaryOperation<TripleExpression> implements TripleExpression {
    public CheckedUnaryOperation(TripleExpression operand) {
        super(operand);
    }

    protected abstract int implementation(int operand);

    @Override
    public int evaluate(int x, int y, int z) {
        return implementation(operand.evaluate(x, y, z));
    }

    protected IntOverflowException overflow(int value) {
        return IntOverflowException.forUnary(operator().symbol(), operand.toMiniString(), value);
    }


    protected NotIntegerResultException notAnInt(int value) {
        return NotIntegerResultException.forUnary(operator().symbol(), operand.toMiniString(), value);
    }
}
