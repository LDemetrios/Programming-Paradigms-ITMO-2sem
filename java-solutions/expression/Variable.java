package expression;

import expression.exceptions.NotEnoughInformationException;

import java.util.Objects;

public record Variable(String name) implements CommonExpressionInterface {
    public Variable {
        Objects.requireNonNull(name);
    }

    @Override
    public int evaluate(int x) {
        if ("x".equals(name)) {
            return x;
        } else {
            throw NotEnoughInformationException.forVariable(name, "x");
        }
    }

    @Override
    public double evaluate(double x) {
        if ("x".equals(name)) {
            return x;
        } else {
            throw NotEnoughInformationException.forVariable(name, "x");
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw NotEnoughInformationException.forVariable(name, "x", "y", "z");
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return name;
    }
}
