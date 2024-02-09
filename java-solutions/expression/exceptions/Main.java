package expression.exceptions;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        TripleExpression expression = new ExpressionParser().parse("1000000*x*x*x*x*x/(x-1)");
        System.out.println("x          f");
        for (int x = 0; x <= 10; x++) {
            try {
                int f = expression.evaluate(x, 0, 0);
                System.out.printf("%-10d %d%n", x, f);
            } catch (DivisionByZeroException e) {
                System.out.printf("%-10d division by zero%n", x);
            } catch (IntOverflowException e) {
                System.out.printf("%-10d overflow%n", x);
            }
        }
    }
}
