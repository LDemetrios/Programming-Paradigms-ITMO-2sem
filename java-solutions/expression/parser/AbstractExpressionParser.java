package expression.parser;

import expression.Const;
import expression.TripleExpression;
import expression.Variable;
import expression.exceptions.ExpressionFormatException;
import expression.exceptions.IntOverflowException;

public abstract class AbstractExpressionParser extends BaseParser {
    private TokenType tokenType = null;
    private String token = null;

    public AbstractExpressionParser(final CharSource source) {
        super(source);
        takeToken();
    }

    public AbstractExpressionParser(final String source) {
        super(source);
        takeToken();
    }

    protected abstract TripleExpression gcdImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression lcmImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression addImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression subImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression mulImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression divImpl(TripleExpression l, TripleExpression r);

    protected abstract TripleExpression pow10Impl(TripleExpression it);

    protected abstract TripleExpression log10Impl(TripleExpression it);

    protected abstract TripleExpression negImpl(TripleExpression it);

    protected abstract TripleExpression revImpl(TripleExpression it);

    public TripleExpression parseExpression() {
        TripleExpression expr = parseExpr();
        check("Extra closing parenthesis", !test(')'), "Try revising the brackets");
        check("What's next?", eofToken(), "Expected EOF or operation here");
        return expr;
    }

    private TripleExpression parseExpr() {
        TripleExpression result = parseClause();
        while (true) {
            if (takeWordToken("gcd")) {
                result = gcdImpl(result, parseClause());
            } else if (takeWordToken("lcm")) {
                result = lcmImpl(result, parseClause());
            } else {
                return result;
            }
        }
    }

    private TripleExpression parseClause() {
        TripleExpression result = parseTerm();
        while (true) {
            if (takeTokenType(TokenType.PLUS)) {
                result = addImpl(result, parseTerm());
            } else if (takeTokenType(TokenType.MINUS)) {
                result = subImpl(result, parseTerm());
            } else {
                return result;
            }
        }
    }

    private TripleExpression parseTerm() {
        TripleExpression result = parseUnit();
        while (true) {
            if (takeTokenType(TokenType.STAR)) {
                result = mulImpl(result, parseUnit());
            } else if (takeTokenType(TokenType.SLASH)) {
                result = divImpl(result, parseUnit());
            } else {
                return result;
            }
        }
    }

    private TripleExpression parseUnit() {
        if (takeTokenType(TokenType.OP_PAR)) {
            TripleExpression res = parseExpr();
            check("Unclosed parenthesis", takeTokenType(TokenType.CL_PAR), "Expected ')' char");
            return res;
        } else if (testTokenType(TokenType.MINUS)) {
            boolean nextNumber = testIsNumeric();
            takeTokenType(); //Skip that -
            return nextNumber ? new Const(parseNumber(true)) : negImpl(parseUnit());
        } else if (testTokenType(TokenType.NUMERIC)) {
            return new Const(parseNumber(false));
        } else if (testTokenType(TokenType.WORD)) {
            if (takeWordToken("reverse")) {
                return revImpl(parseUnit());
            } else if (takeWordToken("pow10")) {
                return pow10Impl(parseUnit());
            } else if (takeWordToken("log10")) {
                return log10Impl(parseUnit());
            } else {
                String token = takeToken();
                if (token.length() == 1 && 'x' <= token.charAt(0) && token.charAt(0) <= 'z') {
                    return new Variable(token);
                }
            }
            throw exception("Can't resolve symbol", null, true, "Only functions: gcd, lcm, log10, pow10, reverse; and variables: x, y, z are supported");
        }
        throw exception("Can't resolve symbol", null, false, "Expected operand");
    }

    private int parseNumber(boolean negate) {
        String s = (negate ? "-" : "") + takeToken();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            String message = s + (s.matches("-?[0-9]+") ?
                    " " + IntOverflowException.EXCEED_INT_RANGE :
                    " doesn't represent an integer number");
            throw exception("Not an int", e, true, message);
        }
    }

    private void check(String title, boolean condition, String message, Object... args) {
        if (!condition) {
            throw exception(title, null, true, message, args);
        }
    }

    private ExpressionFormatException exception(String title, Throwable cause, boolean shiftPosBack, String message, Object... args) {
        return new ExpressionFormatException(
                "%s (сhar %d): ->...%s (%s)".formatted(
                        title,
                        getPos() - (!shiftPosBack || token == null ? 0 : token.length()),
                        (token == null ? "" : token) + tryTake(10),
                        message.formatted(args)),
                cause
        );
    }

    private TokenType nextTokenType() {
        skipWhitespace();
        if (eof()) {
            token = "EOF";
            return TokenType.EOF;
        }
        for (TokenType tt : TokenType.values()) {
            if (take(tt.special)) {
                token = String.valueOf(tt.special);
                return tt;
            }
        }
        if (testIsNumeric()) {
            StringBuilder sb = new StringBuilder();
            while (testIsNumeric()) {
                sb.append(take());
            }
            token = sb.toString();
            return TokenType.NUMERIC;
        } else if (testIsLetter()) {
            StringBuilder sb = new StringBuilder();
            while (testIsLetter() || testIsNumeric()) {
                sb.append(take());
            }
            token = sb.toString();
            return TokenType.WORD;
        } else {
            throw exception("Unsupported char", null, false, "Use 'A'-'Z', 'a'-'z', '_', '+', '-', '*', '/', '(', ')' or '0'-'9");
        }
    }

    private boolean testIsLetter() {
        return between('a', 'z') || between('A', 'Z') || test('_');
    }

    private boolean testIsNumeric() {
        return between('0', '9');
    }

    protected String takeToken() {
        String cur = token;
        tokenType = nextTokenType();
        return cur;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected TokenType takeTokenType() {
        TokenType cur = tokenType;
        tokenType = nextTokenType();
        return cur;
    }

    protected boolean takeWordToken(String expected) {
        if (testTokenType(TokenType.WORD) && expected.equals(token)) {
            takeTokenType();
            return true;
        }
        return false;
    }

    protected boolean testTokenType(TokenType expected) {
        return tokenType == expected;
    }

    protected boolean takeTokenType(TokenType expected) {
        if (testTokenType(expected)) {
            takeTokenType();
            return true;
        }
        return false;
    }

    protected boolean eofToken() {
        return tokenType == TokenType.EOF;
    }

    protected IllegalArgumentException error(String message) {
        return super.error(message);
    }

    private enum TokenType {
        WORD('\0'), //[A-Za-z_][A-Za-z_0-9]*
        NUMERIC('\0'), //[0-9]+
        PLUS('+'), MINUS('-'), STAR('*'), SLASH('/'),
        OP_PAR('('), CL_PAR(')'), EOF('\0');
        public final char special;

        TokenType(char special) {
            this.special = special;
        }
    }
}
