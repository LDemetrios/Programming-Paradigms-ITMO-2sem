package expression.parser;

import expression.TripleExpression;
import expression.general.*;

public class StdExpressionParser extends AbstractExpressionParser {
    public StdExpressionParser(CharSource source) {
        super(source);
    }

    public StdExpressionParser(String source) {
        super(source);
    }

    @Override
    protected TripleExpression gcdImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.GCD, r, SomeMath::gcd);
    }

    @Override
    protected TripleExpression lcmImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.LCM, r, SomeMath::lcm);
    }

    @Override
    protected TripleExpression addImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.ADDITION, r, SomeMath::sum);
    }

    @Override
    protected TripleExpression subImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.SUBTRACTION, r, SomeMath::difference);
    }

    @Override
    protected TripleExpression mulImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.MULTIPLICATION, r, SomeMath::product);
    }

    @Override
    protected TripleExpression divImpl(TripleExpression l, TripleExpression r) {
        return new IntBinaryOperationImpl(l, BinaryOperatorEnumImpl.DIVISION, r, SomeMath::ratio);
    }

    @Override
    protected TripleExpression pow10Impl(TripleExpression it) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected TripleExpression log10Impl(TripleExpression it) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected TripleExpression negImpl(TripleExpression it) {
        return new IntUnaryOperationImpl(it, UnaryOperatorEnumImpl.NEGATION, SomeMath::negated);
    }

    @Override
    protected TripleExpression revImpl(TripleExpression it) {
        return new IntUnaryOperationImpl(it, UnaryOperatorEnumImpl.REVERSE, SomeMath::reverse);
    }
}