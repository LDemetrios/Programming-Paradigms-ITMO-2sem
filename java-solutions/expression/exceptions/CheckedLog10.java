package expression.exceptions;

import expression.TripleExpression;
import expression.general.UnaryOperator;
import expression.general.UnaryOperatorEnumImpl;


public class CheckedLog10 extends CheckedUnaryOperation {

    public CheckedLog10(TripleExpression operand) {
        super(operand);
    }

    @Override
    public UnaryOperator operator() {
        return UnaryOperatorEnumImpl.LOGARITHM10;
    }

    protected int implementation(final int operand) {
        if (operand <= 0) {
            throw NonPositiveNumberLogarithmException.forOperand(this.operand, operand);
        }
        int res = -1;
        int x = operand;
        while (x > 0) {
            res++;
            x /= 10;
        }
        return res;
    }
}
