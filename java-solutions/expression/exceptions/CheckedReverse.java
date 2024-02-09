package expression.exceptions;

import expression.TripleExpression;
import expression.general.UnaryOperator;
import expression.general.UnaryOperatorEnumImpl;


public class CheckedReverse extends CheckedUnaryOperation {
    private static final int MAX_VALUE_TENTH = Integer.MAX_VALUE / 10;

    public CheckedReverse(TripleExpression operand) {
        super(operand);
    }

    @Override
    public UnaryOperator operator() {
        return UnaryOperatorEnumImpl.REVERSE;
    }

    protected int implementation(final int operand) {
        if (operand == Integer.MIN_VALUE) {
            throw overflow(operand);
        }
        int res = 0;
        int sign = operand >= 0 ? 1 : -1;
        int x = Math.abs(operand);
        while (x != 0) {
            if (res > MAX_VALUE_TENTH) {
                throw overflow(operand);
            }
            res *= 10;
            if (res > Integer.MAX_VALUE - x % 10) {
                throw overflow(operand);
            }
            res += x % 10;
            x /= 10;
        }
        return res * sign;
    }
}
