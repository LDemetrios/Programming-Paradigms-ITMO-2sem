package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.stream.Stream;

public class Scanner {
    private static final CharPredicate IS_LINE_SEPARATOR_SYMBOL
            = c -> c == '\n' || c == '\r' || c == '\u2028' || c == '\u2029' || c == '\u0085';
    private final Reader reader;
    private final CharPredicate isDelimiter;
    private final CharPredicate isInLineDelimiter;

    private final char[] buf = new char[1024];
    private int pointer = 0; // Current position
    private int available = 0; // Actual chars in buffer

    public Scanner(Reader reader, CharPredicate delimiters) {
        this.reader = reader;
        this.isDelimiter = delimiters;
        isInLineDelimiter = c -> isDelimiter.test(c) && !IS_LINE_SEPARATOR_SYMBOL.test(c);
    }

    public Scanner(InputStream is, CharPredicate delimiters) {
        this(new InputStreamReader(is), delimiters);
    }

    public Scanner(Reader reader) {
        this(reader, Character::isWhitespace);
    }

    public Scanner(InputStream is) {
        this(new InputStreamReader(is), Character::isWhitespace);
    }

    public Scanner(CharPredicate delimiters) {
        this(System.in, delimiters);
    }

    public Scanner() {
        this(System.in, Character::isWhitespace);
    }

    private boolean fillBuf() {
        if (pointer == buf.length) {
            pointer = 0;
            available = 0;
        }
        try {
            int num = 0;
            while (num == 0) {
                num = reader.read(buf, available, buf.length - available);
            }
            if (num < 0) return false;
            available += num;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean hasNextChar() {
        return pointer < available || fillBuf();
    }

    private char nextChar() {
        if (pointer < available) {
            return buf[pointer++];
        }
        if (!fillBuf()) throw new IllegalStateException();
        return buf[pointer++];
    }

    private void skip(CharPredicate predicate) {
        while (hasNextChar()) {
            char c = nextChar();
            if (!predicate.test(c)) {
                pointer--;
                break;
            }
        }
    }

    public boolean hasNext() {
        skip(isDelimiter);
        return hasNextChar();
    }

    public String next() {
        if (!hasNext()) return null;
        StringBuilder builder = new StringBuilder();
        while (hasNextChar()) {
            char c = nextChar();
            if (isDelimiter.test(c)) {
                pointer--;
                break;
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public boolean hasNextLine() {
        skip(isInLineDelimiter); // In case few spaces left in the line
        return hasNextChar();
    }

    public void skipLineSep() {
        if (!hasNextChar()) return;
        char next = nextChar();
        if (!IS_LINE_SEPARATOR_SYMBOL.test(next)) {
            pointer--;
            return;
        }
        if (next == '\r') {
            if (!hasNextChar()) return;
            char after = nextChar();
            if (after != '\n') {
                pointer--;
            } /* else {
                        Skip it and return nexts
            }*/
        }
    }

    public String nextLine() {
        if (!hasNextLine()) return null;

        skipLineSep();

        StringBuilder builder = new StringBuilder();
        while (hasNextChar()) {
            char c = nextChar();
            if (IS_LINE_SEPARATOR_SYMBOL.test(c)) {
                pointer--;
                break;
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public boolean lineRemains() {
        skip(isInLineDelimiter); // In case few spaces left in the line
        if (!hasNextChar()) return false;
        char next = nextChar();
        pointer--;
        return !IS_LINE_SEPARATOR_SYMBOL.test(next);
    }

    public String wordInLine() {
        return lineRemains() ? next() : null;
    }

    public Stream<String> wordsUntilLineEnd(boolean skipLineSep) {
        return Stream.iterate(wordInLine(), it -> {
            if (it == null && skipLineSep) skipLineSep();
            return it != null;
        }, it -> wordInLine());
    }

    public Stream<Stream<String>> wordsTable() {
        return StreamUtils.stream(this::hasNextLine, () -> this.wordsUntilLineEnd(true));
    }

    public Stream<String> lines() {
        return Stream.iterate(nextLine(), Objects::nonNull, s -> nextLine());
    }

    public Stream<String> words() {
        return Stream.iterate(next(), Objects::nonNull, s -> next());
    }

    public int nextInt() {
        String token = next();
        if (token.length() < 2) return Integer.parseInt(token);
        int radix = switch (token.charAt(1)) {
            case 'b' -> 2;
            case 'o' -> 8;
            case 'x' -> 16;
            default -> 10;
        };
        return radix == 10 ? Integer.parseInt(token) : Integer.parseInt(token.substring(2), radix);
    }

    @Override
    public String toString() {
        return "util.Scanner: [" +
                new String(buf, 0, pointer) +
                " /pointer/ " +
                new String(buf, pointer, available) +
                "...]";
    }

    @FunctionalInterface
    public interface CharPredicate {
        boolean test(char c);
    }
}
