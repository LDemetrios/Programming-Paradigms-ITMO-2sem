package expression;

import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;

public class Multiply extends CommonBinaryOperation {
    public Multiply(CommonExpressionInterface left, CommonExpressionInterface right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        return left * right;
    }

    @Override
    protected double implementation(double left, double right) {
        return left * right;
    }

//    @Override
//    protected boolean parenthesizeRight() {
//        return super.parenthesizeRight() && !(right instanceof Multiply);
//    }


    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.MULTIPLICATION;
    }
}
