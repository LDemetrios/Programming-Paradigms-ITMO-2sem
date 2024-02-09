package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;
import expression.general.SomeMath;

public class CheckedGcd extends CheckedBinaryOperation {
    public CheckedGcd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        //noinspection SuspiciousNameCombination
        return SomeMath.gcd(left, right);
    }


    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.GCD;
    }
}
