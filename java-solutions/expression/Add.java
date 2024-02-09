package expression;

import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class Add extends CommonBinaryOperation {
    public Add(CommonExpressionInterface left, CommonExpressionInterface right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        return left + right;
    }

//    @Override
//    protected boolean parenthesizeRight() {
//        return super.parenthesizeRight() && !(right instanceof Add || right instanceof Subtract);
//    }

    @Override
    protected double implementation(double left, double right) {
        return left + right;
    }

    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.ADDITION;
    }
}
