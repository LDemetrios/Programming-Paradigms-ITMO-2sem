package expression.generic.algebras;

import expression.generic.Algebra;

public class DoubleAlgebra implements Algebra<Double> {
    @Override
    public Double negated(Double x) {
        return -x;
    }

    @Override
    public Double sum(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double diff(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double prod(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double ratio(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double ofString(String x) {
        return Double.valueOf(x);
    }

    @Override
    public Double ofInt(int x) {
        return (double) x;
    }

    @Override
    public Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    public Double mod(Double x, Double y) {
        return x % y;
    }
}
