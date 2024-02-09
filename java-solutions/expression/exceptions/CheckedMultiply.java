package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class CheckedMultiply extends CheckedBinaryOperation {
    public CheckedMultiply(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        if (left == -1 && right == Integer.MIN_VALUE || left == Integer.MIN_VALUE && right == -1) {
            throw overflow(left, right);
        }
        if (left == 0 || right == 0) {
            return 0;
        }
        int res = left * right;
        if (res / right != left) {
            throw overflow(left, right);
        }
        return res;
    }


    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.MULTIPLICATION;
    }
}
