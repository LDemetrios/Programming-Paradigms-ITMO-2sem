package expression;

import util.InStreamUtil;

import static util.InStreamUtil.TokenType.INT;

public class Main {
    public static void main(String[] args) {
        InStreamUtil.request("Enter x:", true, INT);
        int xValue = InStreamUtil.getInt();

        Variable x = new Variable("x");
        Expression polynome = new Add(
                new Subtract(
                        new Multiply(x, x),
                        new Multiply(new Const(2), x)
                ),
                new Const(1)
        );

        System.out.println(polynome.evaluate(xValue));
    }
}
