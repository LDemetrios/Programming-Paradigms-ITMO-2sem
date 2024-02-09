package expression;

import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class Divide extends CommonBinaryOperation {
    public Divide(CommonExpressionInterface left, CommonExpressionInterface right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        return left / right;
    }

    @Override
    protected double implementation(double left, double right) {
        return left / right;
    }

    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.DIVISION;
    }
}
