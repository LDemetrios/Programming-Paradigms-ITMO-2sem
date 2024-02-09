package expression.generic;

import expression.exceptions.ExpressionFormatException;
import expression.parser.BaseParser;
import expression.parser.CharSource;

public class GenericExpressionParser<N> extends BaseParser {
    private final Algebra<N> algebra;

    public GenericExpressionParser(final CharSource source, final Algebra<N> algebra) {
        super(source);
        this.algebra = algebra;
    }

    public GenericExpressionParser(final String source, final Algebra<N> algebra) {
        super(source);
        this.algebra = algebra;
    }

    public GenericExpression<N> parseExpression() {
        GenericExpression<N> expr = parseExpr();
        check("Extra closing parenthesis", !test(')'), "Try revising the brackets");
        check("What's next?", eof(), "Expected EOF or operation here");
        return expr;
    }

    private GenericExpression<N> parseExpr() {
        GenericExpression<N> result = parseTerm();
        while (true) {
            skipWhitespace();
            if (takeToken("+")) {
                result = GenericExpression.ofBinary(algebra::sum, result, parseTerm());
            } else if (takeToken("-")) {
                result = GenericExpression.ofBinary(algebra::diff, result, parseTerm());
            } else {
                return result;
            }
        }
    }

    private GenericExpression<N> parseTerm() {
        GenericExpression<N> result = parseClause();
        while (true) {
            skipWhitespace();
            if (takeToken("*")) {
                result = GenericExpression.ofBinary(algebra::prod, result, parseClause());
            } else if (takeToken("/")) {
                result = GenericExpression.ofBinary(algebra::ratio, result, parseClause());
            } else {
                return result;
            }
        }
    }

    private GenericExpression<N> parseClause() {
        GenericExpression<N> result = parseUnit();
        while (true) {
            skipWhitespace();
            if (takeWord("mod")) {
                result = GenericExpression.ofBinary(algebra::mod, result, parseUnit());
            } else {
                return result;
            }
        }
    }


    private GenericExpression<N> parseUnit() {
        skipWhitespace();
        if (take('(')) {
            GenericExpression<N> res = parseExpr();
            check("Unclosed parenthesis", takeToken(")"), "Expected ')' char");
            return res;
        } else if (take('-')) {
            boolean nextNumber = between('0', '9');
            return nextNumber ?
                    GenericExpression.ofConst(parseNumber(true)) :
                    GenericExpression.ofUnary(algebra::negated, parseUnit());
        } else if (between('0', '9')) {
            return GenericExpression.ofConst(parseNumber(false));
        } else {
            if (takeWord("abs")) {
                return GenericExpression.ofUnary(algebra::abs, parseUnit());
            } else if (takeWord("square")) {
                return GenericExpression.ofUnary(algebra::square, parseUnit());
            } else if ((between('x', 'z'))) {
                return GenericExpression.ofVariable(take());
            }
            throw exception("Can't resolve symbol", null, "Only functions: gcd, lcm, log10, pow10, reverse; and variables: x, y, z are supported");
        }
    }

    private N parseNumber(boolean negate) {
        StringBuilder sb = new StringBuilder(negate ? "-" : "");
        while (between('0', '9')) {
            sb.append(take());
        }
        return algebra.ofString(sb.toString());
    }

    private void check(String title, boolean condition, String message, Object... args) {
        if (!condition) {
            throw exception(title, null, message, args);
        }
    }

    private ExpressionFormatException exception(String title, Throwable cause, String message, Object... args) {
        return new ExpressionFormatException(
                "%s (сhar %d): ->...%s (%s)".formatted(
                        title,
                        getPos(),
                        tryTake(10),
                        message.formatted(args)),
                cause
        );
    }

    private boolean testIsLetter() {
        return between('a', 'z') || between('A', 'Z') || test('_');
    }

    protected IllegalArgumentException error(String message) {
        return super.error(message);
    }

    private boolean takeWord(String word) {
        if (!takeToken(word)) {
            return false;
        }
        check("Incorrect token", !testIsLetter(), "expected %s", word);
        return true;
    }

    private boolean takeToken(String word) {
        if (!test(word.charAt(0))) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (!take(word.charAt(i))) {
                throw exception("Incorrect token", null, "expected %s", word);
            }
        }
        return true;
    }
}
