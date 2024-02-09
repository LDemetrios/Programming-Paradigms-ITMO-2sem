package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class CheckedAdd extends CheckedBinaryOperation {
    public CheckedAdd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        int result = left + right;
        int validation = (left >= 0 ? 1 : -1) + (right >= 0 ? 1 : -1) - (result >= 0 ? 1 : -1);
        if (validation == 3 || validation == -3) {
            throw overflow(left, right);
        }
        return result;
    }


    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.ADDITION;
    }
}
