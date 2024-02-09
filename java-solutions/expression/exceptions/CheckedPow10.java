package expression.exceptions;

import expression.TripleExpression;
import expression.general.UnaryOperator;
import expression.general.UnaryOperatorEnumImpl;


public class CheckedPow10 extends CheckedUnaryOperation {
    private static final int MAX_POW = 9;

    public CheckedPow10(TripleExpression operand) {
        super(operand);
    }

    @Override
    public UnaryOperator operator() {
        return UnaryOperatorEnumImpl.POWER10;
    }

    protected int implementation(final int operand) {
        if (operand < 0) {
            throw NegativeNumberPowerException.forOperand(this.operand, operand);
        }
        if (operand > MAX_POW) {
            throw overflow(operand);
        }
        int res = 1;
        for (int i = 0; i < operand; i++) {
            res *= 10;
        }
        return res;
    }
}
