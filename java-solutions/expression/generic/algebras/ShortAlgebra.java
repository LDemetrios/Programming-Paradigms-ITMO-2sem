package expression.generic.algebras;

import expression.generic.Algebra;

public class ShortAlgebra implements Algebra<Short> {
    @Override
    public Short negated(Short x) {
        return (short) -x;
    }

    @Override
    public Short sum(Short x, Short y) {
        return (short) (x + y);
    }

    @Override
    public Short diff(Short x, Short y) {
        return (short) (x - y);
    }

    @Override
    public Short prod(Short x, Short y) {
        return (short) (x * y);
    }

    @Override
    public Short ratio(Short x, Short y) {
        if (y == 0) return null;
        return (short) (x / y);
    }

    @Override
    public Short ofString(String x) {
        return Short.valueOf(x);
    }

    @Override
    public Short ofInt(int x) {
        return (short) x;
    }

    @Override
    public Short abs(Short x) {
        return (short) Math.abs(x);
    }

    @Override
    public Short mod(Short x, Short y) {
        if (y == 0) return null;
        return (short) (x % y);
    }
}
