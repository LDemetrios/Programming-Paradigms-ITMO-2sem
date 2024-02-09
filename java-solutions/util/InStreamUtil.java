package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.NoSuchElementException;

public final class InStreamUtil {
    private static final Reader in = new BufferedReader(new InputStreamReader(System.in));

    private static int[] intBuf = new int[4];
    private static long[] longBuf = new long[4];
    private static double[] doubleBuf = new double[4];
    private static String[] stringBuf = new String[4];

    private static int intBufPointer = 0;
    private static int longBufPointer = 0;
    private static int doubleBufPointer = 0;
    private static int stringBufPointer = 0;

    private static int intsPresent = 0;
    private static int longsPresent = 0;
    private static int doublesPresent = 0;
    private static int stringsPresent = 0;

    private InStreamUtil() {
    }

    public static void request(String message, boolean thenSkipLineSep, TokenType... tokenTypes) {
        intBufPointer = 0;
        longBufPointer = 0;
        doubleBufPointer = 0;
        stringBufPointer = 0;
        boolean ok;
        do {
            intsPresent = 0;
            longsPresent = 0;
            doublesPresent = 0;
            stringsPresent = 0;
            ok = true;
            if (message != null) {
                System.out.println(message);
            }
            String[] tokens = getNTokens(tokenTypes.length, thenSkipLineSep);
            for (int i = 0; i < tokenTypes.length; i++) {
                boolean success = switch (tokenTypes[i]) {
                    case INT -> processInt(tokens[i]);
                    case LONG -> processLong(tokens[i]);
                    case DOUBLE -> processDouble(tokens[i]);
                    case WORD -> processString(tokens[i]);
                };
                if (!success) {
                    System.out.printf(
                            "Sorry, %s token (%s) couldn't be interpreted as %s. Try again.%n",
                            Utils.ordinal(i + 1),
                            tokens[i],
                            tokenTypes[i].toString().toLowerCase()
                    );
                    ok = false;
                    break;
                }
            }
        } while (!ok);
    }

    private static String[] getNTokens(int length, boolean thenSkipLineSep) {
        String[] res = new String[length];
        boolean wasLineSep = false;
        for (int i = 0; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            try {
                boolean start = true;
                while (true) {
                    int c = in.read();
                    if (start && Character.isWhitespace(c)) {
                        continue;
                    }
                    start = false;

                    if (c == -1) break;
                    if (c == '\\') {
                        int next = in.read();
                        if (next == -1) break;
                        sb.append((char) next);
                    }
                    if (Character.isWhitespace(c)) {
                        wasLineSep = (c == '\n' || c == '\r' || c == '\u2028' || c == '\u2029' || c == '\u0085');
                        break;
                    } else {
                        sb.append((char) c);
                    }
                }
            } catch (IOException e) {
                res[i] = sb.toString();
                return res;
            }
            res[i] = sb.toString();
        }
        if (thenSkipLineSep && !wasLineSep) {
            String linesep = System.lineSeparator();
            try {
                int c;
                do {
                    c = in.read();
                } while (c != -1 && c != '\n' && c != '\r' && c != '\u2028' && c != '\u2029' && c != '\u0085');
            } catch (IOException e) {
                return res;
            }
        }
        return res;
    }

    private static boolean processInt(String token) {
        // Integer.parseInt(token, 10); with exceptions replaced by "return false"

        boolean negative = false;
        int i = 0, len = token.length();
        int limit = -Integer.MAX_VALUE;

        if (len <= 0) {
            return false;
        }
        char firstChar = token.charAt(0);
        if (firstChar < '0') { // Possible leading "+" or "-"
            if (firstChar == '-') {
                negative = true;
                limit = Integer.MIN_VALUE;
            } else if (firstChar != '+') {
                return false;
            }

            if (len == 1) { // Cannot have lone "+" or "-"
                return false;
            }
            i++;
        }
        int multmin = limit / 10;
        int result = 0;
        while (i < len) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            int digit = Character.digit(token.charAt(i++), 10);
            if (digit < 0 || result < multmin) {
                return false;
            }
            result *= 10;
            if (result < limit + digit) {
                return false;
            }
            result -= digit;
        }
        result = negative ? result : -result;

        if (intBuf.length == intsPresent) {
            intBuf = Arrays.copyOf(intBuf, intBuf.length * 2);
        }
        intBuf[intsPresent++] = result;
        return true;
    }

    private static boolean processLong(String token) {
        // Long.parseLong(token, 10); with exceptions replaced by "return false"

        if (token == null) {
            return false;
        }

        boolean negative = false;
        int i = 0, len = token.length();
        long limit = -Long.MAX_VALUE;

        if (len <= 0) {
            return false;
        }
        char firstChar = token.charAt(0);
        if (firstChar < '0') { // Possible leading "+" or "-"
            if (firstChar == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
            } else if (firstChar != '+') {
                return false;
            }

            if (len == 1) { // Cannot have lone "+" or "-"
                return false;
            }
            i++;
        }
        long multmin = limit / 10;
        long result = 0;
        while (i < len) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            int digit = Character.digit(token.charAt(i++), 10);
            if (digit < 0 || result < multmin) {
                return false;
            }
            result *= 10;
            if (result < limit + digit) {
                return false;
            }
            result -= digit;
        }
        result = negative ? result : -result;

        if (longBuf.length == longsPresent) {
            longBuf = Arrays.copyOf(longBuf, longBuf.length * 2);
        }
        longBuf[longsPresent++] = result;
        return true;
    }

    private static boolean processDouble(String token) {
        try { //No way...
            double res = Double.parseDouble(token);

            if (doubleBuf.length == doublesPresent) {
                doubleBuf = Arrays.copyOf(doubleBuf, doubleBuf.length * 2);
            }
            doubleBuf[doublesPresent++] = res;
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static boolean processString(String token) {
        if (stringBuf.length == stringsPresent) {
            stringBuf = Arrays.copyOf(stringBuf, stringBuf.length * 2);
        }
        stringBuf[stringsPresent++] = token;
        return true;
    }

    public static int getInt() {
        if (intBufPointer < intsPresent) {
            return intBuf[intBufPointer++];
        }
        throw new NoSuchElementException();
    }

    public static long getLong() {
        if (longBufPointer < longsPresent) {
            return longBuf[longBufPointer++];
        }
        throw new NoSuchElementException();
    }

    public static double getDouble() {
        if (doubleBufPointer < doublesPresent) {
            return doubleBuf[doubleBufPointer++];
        }
        throw new NoSuchElementException();
    }

    public static String getWord() {
        if (stringBufPointer < stringsPresent) {
            return stringBuf[stringBufPointer++];
        }
        throw new NoSuchElementException();
    }

    public enum TokenType {
        INT, LONG, DOUBLE, WORD
    }
}

