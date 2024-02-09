package expression.general;

public interface BinaryOperator extends Operator {
    @Override
    int priority();

    @Override
    String symbol();

    boolean associative();

    BinaryOperator inverse();
}
