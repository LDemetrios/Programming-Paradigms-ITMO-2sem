package expression.parser;

@SuppressWarnings({"SameParameterValue"})
public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;
    private char last = 0xffff;
    private long pos = -1;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected BaseParser(final String source) {
        this(new StringSource(source));
    }

    protected char take() {
        last = ch;
        ch = source.hasNext() ? source.next() : END;
        if (ch != END) pos++;
        return last;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected int skipWhitespace() {
        int skipped = 0;
        while (!eof() && Character.isWhitespace(ch)) {
            skipped++;
            take();
        }
        return skipped;
    }

    protected String cantResolve(String additionalMessage) {
        final int charsToGet = 10;
        StringBuilder sb = new StringBuilder("Can't resolve symbol (char " + getPos() + "): ->...");
        sb.append(tryTake(charsToGet));
        if (eof()) {
            sb.append("$EOF");
        }
        if (additionalMessage != null) {
            sb.append(" (");
            sb.append(additionalMessage);
            sb.append(")");
        }
        return sb.toString();
    }

    protected long getPos() {
        return pos;
    }

    protected String tryTake(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count && !eof(); i++) {
            sb.append(take());
        }
        return sb.toString();
    }

    protected char lastReturned() {
        return last;
    }

    protected char peek() {
        return ch;
    }
}
