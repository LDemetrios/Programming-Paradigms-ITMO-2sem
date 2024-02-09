package expression.generic.algebras;

import expression.generic.Algebra;

public class ModularAlgebra implements Algebra<Integer> {
    private final int mod;

    public ModularAlgebra(int mod) {
        if (mod <= 0) {
            throw new IllegalArgumentException("There is no algebra modulo " + mod);
        }
        this.mod = mod;
    }

    @Override
    public Integer negated(Integer x) {
        return x == 0 ? 0 : mod - x;
    }

    @Override
    public Integer sum(Integer x, Integer y) {
        return (x + y) % mod;
    }

    @Override
    public Integer diff(Integer x, Integer y) {
        return (x - y + mod) % mod;
    }

    @Override
    public Integer prod(Integer x, Integer y) {
        return (x * y) % mod;
    }

    private int pow(int base, int p) {
        int res = 1;
        while (p > 0) {
            if (p % 2 == 1) res = (res * base) % mod;
            base = (base * base) % mod;
            p /= 2;
        }
        return res;
    }

    @Override
    public Integer ratio(Integer x, Integer y) {
        if (y == 0) return null;
        int yInv = pow(y, mod - 2);
        return (x * yInv) % mod;
    }

    @Override
    public Integer ofString(String x) {
        return (Integer.parseInt(x) % mod + mod) % mod;
    }

    @Override
    public Integer ofInt(int x) {
        return (x % mod + mod) % mod;
    }

    @Override
    public Integer abs(Integer x) {
        return x;
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y == 0) return null;
        return x % y;
    }
}
