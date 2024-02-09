package expression.exceptions;

import expression.TripleExpression;
import expression.general.BinaryOperator;
import expression.general.BinaryOperatorEnumImpl;
import expression.general.SomeMath;

public class CheckedLcm extends CheckedBinaryOperation {
    public CheckedLcm(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int implementation(int left, int right) {
        if (left == 0 || right == 0) {
            return 0;
        }
        if (left == Integer.MIN_VALUE && right == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE; //Temp
        }
        if (left < 0 && right == Integer.MIN_VALUE || right < 0 && left == Integer.MIN_VALUE) {
            throw overflow(left, right);
        }
        @SuppressWarnings("SuspiciousNameCombination") int res = SomeMath.lcm(left, right);
        if (res % left == 0 && res % right == 0) {
            return res;
        }
        throw overflow(left, right);
    }


    @Override
    public BinaryOperator operator() {
        return BinaryOperatorEnumImpl.LCM;
    }
}
