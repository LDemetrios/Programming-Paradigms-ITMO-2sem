package expression.general;

import expression.TripleExpression;

import java.util.function.IntBinaryOperator;

public class IntBinaryOperationImpl extends BinaryOperation<TripleExpression> implements TripleExpression {
    public final IntBinaryOperator impl;
    public final BinaryOperator operator;

    public IntBinaryOperationImpl(TripleExpression left, BinaryOperator op, TripleExpression right, IntBinaryOperator impl) {
        super(left, right);
        this.impl = impl;
        this.operator = op;
    }

    @Override
    public BinaryOperator operator() {
        return operator;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return impl.applyAsInt(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
