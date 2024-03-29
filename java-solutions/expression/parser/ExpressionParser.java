package expression.parser;

import expression.TripleExpression;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) {
        return new StdExpressionParser(expression).parseExpression();
    }
}
