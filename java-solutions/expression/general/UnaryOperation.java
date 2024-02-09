package expression.general;

import expression.Const;
import expression.ToMiniString;
import expression.Variable;

import java.util.Objects;

public abstract class UnaryOperation<E extends ToMiniString> implements Operation {
    protected final E operand;

    public UnaryOperation(E left) {
        this.operand = Objects.requireNonNull(left);
    }

    @Override
    public abstract UnaryOperator operator();

    @Override
    public final String toString() {
        return "" + operator().symbol() + "(" + operand + ")";
    }

    @Override
    public String toMiniString() {
        boolean parenthesize = parenthesize();
        String op = operator().symbol();
        String arg = operand.toMiniString();

        return op + (parenthesize ? "(" : " ") + arg + (parenthesize ? ")" : "");
    }

    protected boolean parenthesize() {
        return !(operand instanceof Const || operand instanceof Variable ||
                (operand instanceof Operation o && o.operator().priority() >= operator().priority()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryOperation<?> that = (UnaryOperation<?>) o;
        return operator().equals(that.operator()) && operand.equals(that.operand);
    }

    @Override
    public int hashCode() {
        final int coeff = 31;
        return operand.hashCode() * coeff + operator().hashCode();
    }
}
