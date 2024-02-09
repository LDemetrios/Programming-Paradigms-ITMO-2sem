package expression.exceptions;

import expression.TripleExpression;
import expression.parser.AbstractExpressionParser;
import expression.parser.CharSource;

public class ExpressionParserCheckedImpl extends AbstractExpressionParser {
    public ExpressionParserCheckedImpl(CharSource source) {
        super(source);
    }

    public ExpressionParserCheckedImpl(String source) {
        super(source);
    }

    @Override
    protected TripleExpression gcdImpl(TripleExpression l, TripleExpression r) {
        return new CheckedGcd(l, r);
    }

    @Override
    protected TripleExpression lcmImpl(TripleExpression l, TripleExpression r) {
        return new CheckedLcm(l, r);
    }

    @Override
    protected TripleExpression addImpl(TripleExpression l, TripleExpression r) {
        return new CheckedAdd(l, r);
    }

    @Override
    protected TripleExpression subImpl(TripleExpression l, TripleExpression r) {
        return new CheckedSubtract(l, r);
    }

    @Override
    protected TripleExpression mulImpl(TripleExpression l, TripleExpression r) {
        return new CheckedMultiply(l, r);
    }

    @Override
    protected TripleExpression divImpl(TripleExpression l, TripleExpression r) {
        return new CheckedDivide(l, r);
    }

    @Override
    protected TripleExpression pow10Impl(TripleExpression it) {
        return new CheckedPow10(it);
    }

    @Override
    protected TripleExpression log10Impl(TripleExpression it) {
        return new CheckedLog10(it);
    }

    @Override
    protected TripleExpression negImpl(TripleExpression it) {
        return new CheckedNegate(it);
    }

    @Override
    protected TripleExpression revImpl(TripleExpression it) {
        return new CheckedReverse(it);
    }
}
