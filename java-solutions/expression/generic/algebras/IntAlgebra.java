package expression.generic.algebras;

import expression.generic.Algebra;

public class IntAlgebra implements Algebra<Integer> {
    @Override
    public Integer negated(Integer x) {
        return -x;
    }

    @Override
    public Integer sum(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer diff(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer prod(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer ratio(Integer x, Integer y) {
        if (y == 0) return null;
        return x / y;
    }

    @Override
    public Integer ofString(String x) {
        return Integer.valueOf(x);
    }

    @Override
    public Integer ofInt(int x) {
        return x;
    }

    @Override
    public Integer abs(Integer x) {
        return Math.abs(x);
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y == 0) return null;
        return x % y;
    }
}
