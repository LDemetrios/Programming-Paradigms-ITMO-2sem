package expression.generic.algebras;

import expression.generic.Algebra;

public class CheckedIntAlgebra implements Algebra<Integer> {
    @Override
    public Integer negated(Integer x) {
        if (x == Integer.MIN_VALUE) {
            return null;
        }
        return -(int) x;
    }

    @Override
    public Integer sum(Integer x, Integer y) {
        int r = x + (int) y;
        // HD 2-12 Overflow iff both arguments have the opposite sign of the result
        if (((x ^ r) & (y ^ r)) < 0) {
            return null;
        }
        return r;
    }

    @Override
    public Integer diff(Integer x, Integer y) {
        int r = x - (int) y;
        // HD 2-12 Overflow iff the arguments have different signs and
        // the sign of the result is different from the sign of x
        if (((x ^ (int) y) & (x ^ r)) < 0) {
            return null;
        }
        return r;
    }

    @Override
    public Integer prod(Integer x, Integer y) {
        long r = (long) (int) x * (long) (int) y;
        if ((int) r != r) {
            return null;
        }
        return (int) r;
    }

    @Override
    public Integer ratio(Integer x, Integer y) {
        if (y == 0) return null;
        int q = x / (int) y;
        if ((x & y & q) >= 0) {
            return q;
        }
        return null;
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
        return (x == Integer.MIN_VALUE) ? null : Math.abs(x);
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y == 0) return null;
        return x % y;
    }
}
