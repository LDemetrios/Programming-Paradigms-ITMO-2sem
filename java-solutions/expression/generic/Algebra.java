package expression.generic;

public interface Algebra<N> {
    N negated(N x);

    N sum(N x, N y);

    N diff(N x, N y);

    N prod(N x, N y);

    N ratio(N x, N y);

    N ofString(String x);

    N ofInt(int x);

    default N square(N x) {
        return prod(x, x);
    }

    N abs(N x);

    N mod(N x, N y);
}
