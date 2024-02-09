package expression.general;

import expression.TripleExpression;

import java.util.function.IntUnaryOperator;

public class IntUnaryOperationImpl extends UnaryOperation<TripleExpression> implements TripleExpression {
    public final IntUnaryOperator impl;
    public final UnaryOperator operator;

    public IntUnaryOperationImpl(TripleExpression left, UnaryOperator operator, IntUnaryOperator impl) {
        super(left);
        this.impl = impl;
        this.operator = operator;
    }

    @Override
    public UnaryOperator operator() {
        return operator;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return impl.applyAsInt(operand.evaluate(x, y, z));
    }
}
