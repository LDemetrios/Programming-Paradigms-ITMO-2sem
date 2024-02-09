package expression;

import expression.general.BinaryOperation;


public abstract class CommonBinaryOperation extends BinaryOperation<CommonExpressionInterface> implements CommonExpressionInterface {
    public CommonBinaryOperation(CommonExpressionInterface left,
                                 CommonExpressionInterface right) {
        super(left, right);
    }

    protected abstract int implementation(int left, int right);

    protected abstract double implementation(double left, double right);

    @Override
    public int evaluate(int x) {
        return implementation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return implementation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double x) {
        return implementation(left.evaluate(x), right.evaluate(x));
    }
}
