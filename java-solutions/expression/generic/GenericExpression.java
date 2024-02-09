package expression.generic;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface GenericExpression<N> {
    N evaluate(N x, N y, N z);

    static <N> GenericExpression<N> ofUnary(Function<N, N> operation, GenericExpression<N> operand) {
        return (x, y, z) -> {
            N operandValue = operand.evaluate(x, y, z);
            return operandValue == null ? null : operation.apply(operandValue);
        };
    }

    static <N> GenericExpression<N> ofBinary(
            BiFunction<N, N, N> operation,
            GenericExpression<N> left,
            GenericExpression<N> right
    ) {
        return (x, y, z) -> {
            N leftValue = left.evaluate(x, y, z);
            if (leftValue == null) return null;
            N rightValue = right.evaluate(x, y, z);
            if (rightValue == null) return null;
            return operation.apply(leftValue, rightValue);
        };
    }

    static <N> GenericExpression<N> ofConst(N c) {
        return (x, y, z) -> c;
    }

    static <N> GenericExpression<N> ofVariable(char variable) {
        return switch (variable) {
            case 'x' -> (x, y, z) -> x;
            case 'y' -> (x, y, z) -> y;
            case 'z' -> (x, y, z) -> z;
            default -> (x, y, z) -> null;
        };
    }
}
