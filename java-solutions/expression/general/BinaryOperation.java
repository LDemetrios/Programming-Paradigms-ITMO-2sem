package expression.general;

import expression.ToMiniString;

import java.util.Objects;

public abstract class BinaryOperation<E extends ToMiniString> implements Operation {
    protected final E left;
    protected final E right;

    public BinaryOperation(E left, E right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Override
    public abstract BinaryOperator operator();

    @Override
    public final String toString() {
        return "(" + left + " " + operator().symbol() + " " + right + ")";
    }

    @Override
    public String toMiniString() {
        return parenthesizeLeft() ?
                parenthesizeRight() ?
                        ("(" + left.toMiniString() + ") " + operator().symbol() + " (" + right.toMiniString() + ")") :
                        ("(" + left.toMiniString() + ") " + operator().symbol() + " " + right.toMiniString()) :
                parenthesizeRight() ?
                        (left.toMiniString() + " " + operator().symbol() + " (" + right.toMiniString() + ")") :
                        (left.toMiniString() + " " + operator().symbol() + " " + right.toMiniString());
    }

    protected boolean parenthesizeLeft() {
        return left instanceof Operation l && l.operator().priority() < operator().priority();
    }

    protected boolean parenthesizeRight() {
        if (right instanceof BinaryOperation<?> r) {
            BinaryOperator op = operator();
            BinaryOperator rOp = r.operator();
            return rOp.priority() <= op.priority() && !(rOp.equals(op) && op.associative()) && !rOp.equals(op.inverse());
        } else if (right instanceof Operation r) {
            Operator op = operator();
            Operator rOp = r.operator();
            return rOp.priority() <= op.priority();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperation<?> that = (BinaryOperation<?>) o;
        return operator().equals(that.operator()) && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        final int coeff = 31;
        return (left.hashCode() * coeff + right.hashCode()) * coeff + operator().hashCode();
    }
}
