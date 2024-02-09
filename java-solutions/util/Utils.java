package util;

public class Utils {
    public static String ordinal(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("i expected to be non-negative");
        }
        if ((i % 100) / 10 == 1) {
            return i + "-th";
        } else if (i % 10 == 1) {
            return i + "-st";
        } else if (i % 10 == 2) {
            return i + "-nd";
        } else if (i % 10 == 3) {
            return i + "-rd";
        } else {
            return i + "-th";
        }
    }

    public static String naturallyJoin(Object... args) {
        if (args == null)
            return "null";

        if (args.length == 0)
            return "none";

        if (args.length == 1)
            return args[0].toString();

        int until = args.length - 2;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < until; i++) {
            b.append(args[i]);
            b.append(", ");

        }
        b.append(args[until]);
        b.append(" and ");
        b.append(args[until + 1]);
        return b.toString();
    }
}
