package expression.generic.algebras;

import expression.generic.Algebra;

import java.math.BigInteger;

public class BigIntegerAlgebra implements Algebra<BigInteger> {
    @Override
    public BigInteger negated(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger sum(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger diff(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger prod(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger ratio(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.ZERO)) return null;
        return x.divide(y);
    }

    @Override
    public BigInteger ofString(String x) {
        return new BigInteger(x);
    }

    @Override
    public BigInteger ofInt(int x) {
        return BigInteger.valueOf(x);
    }

    @Override
    public BigInteger abs(BigInteger x) {
        return x.abs();
    }

    @Override
    public BigInteger mod(BigInteger x, BigInteger y) {
        if (y.signum() <= 0) return null;
        return x.mod(y);
    }
}
