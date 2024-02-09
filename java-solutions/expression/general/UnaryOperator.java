package expression.general;

public interface UnaryOperator extends Operator {
    @Override
    int priority();

    @Override
    String symbol();
}
