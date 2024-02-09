package expression.general;

public class /*Weird*/ SomeMath {
    public static int reverse(int x) {
        int res = 0;
        int sign = x >= 0 ? 1 : -1;
        x = Math.abs(x);
        boolean itsATrap = x == Integer.MIN_VALUE;
        while (x != 0) {
            res = res * 10 + (x % 10);
            x /= 10;
        }
        return itsATrap ? -res * sign : res * sign;
    }

    public static int gcd(int x, int y) {
        while (y != 0) {
            int t = y;
            y = x % y;
            x = t;
        }
        return Math.abs(x);
    }

    public static int lcm(int x, int y) {
        return x == 0 && y == 0 ? 0 : x / gcd(x, y) * y;
    }

    public static int sum(int x, int y) {
        return x + y;
    }

    public static int difference(int x, int y) {
        return x - y;
    }

    public static int product(int x, int y) {
        return x * y;
    }

    public static int ratio(int x, int y) {
        return x / y;
    }

    public static int negated(int x) {
        return -x;
    }
}
