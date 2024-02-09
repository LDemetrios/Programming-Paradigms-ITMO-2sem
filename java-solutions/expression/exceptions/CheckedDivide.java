package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class CheckedDivide extends CheckedBinaryOperation {
    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        if (right == 0) {
            throw DivisionByZeroException.forBoth(this.left, this.right);
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw overflow(left, right);
        }
        return left / right;
    }

    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.DIVISION;
    }
}
