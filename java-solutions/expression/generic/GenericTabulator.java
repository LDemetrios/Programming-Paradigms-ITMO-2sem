package expression.generic;

import expression.generic.algebras.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Algebra<?>> algebras = Map.of(
            "i", new CheckedIntAlgebra(),
            "d", new DoubleAlgebra(),
            "bi", new BigIntegerAlgebra(),
            "u", new IntAlgebra(),
            "s", new ShortAlgebra(),
            "p", new ModularAlgebra(10079)
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Algebra<?> algebra = algebras.get(mode);
        if (algebra == null) {
            throw new IllegalArgumentException("Unsupported mode: " + mode);
        }
        return tabulate(algebra, expression, x1, x2, y1, y2, z1, z2);
    }

    private <N> Object[][][] tabulate(
            Algebra<N> algebra, String expressionStr,
            int x1, int x2, int y1, int y2, int z1, int z2
    ) {
        GenericExpression<N> expression = new GenericExpressionParser<>(expressionStr, algebra).parseExpression();
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    result[x - x1][y - y1][z - z1] = expression.evaluate(
                            algebra.ofInt(x), algebra.ofInt(y), algebra.ofInt(z)
                    );
                }
            }
        }
        return result;
    }
}
