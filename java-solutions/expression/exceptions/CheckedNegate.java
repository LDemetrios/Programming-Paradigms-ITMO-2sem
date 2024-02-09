package expression.exceptions;

import expression.TripleExpression;
import expression.general.UnaryOperator;
import expression.general.UnaryOperatorEnumImpl;


public class CheckedNegate extends CheckedUnaryOperation {
    public CheckedNegate(TripleExpression operand) {
        super(operand);
    }

    @Override
    public UnaryOperator operator() {
        return UnaryOperatorEnumImpl.NEGATION;
    }

    protected int implementation(int operand) {
        if (operand == Integer.MIN_VALUE) {
            throw overflow(operand);
        }
        return -operand;
    }
}
