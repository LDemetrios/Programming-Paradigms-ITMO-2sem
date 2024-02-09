package expression;


public class Const implements CommonExpressionInterface {
    private final int intValue;
    private final double doubleValue;
    private final boolean initializedInt;

    public Const(int value) {
        initializedInt = true;
        intValue = value;
        doubleValue = value;
    }

    public Const(double value) {
        initializedInt = false;
        this.intValue = (int) value;
        this.doubleValue = value;
    }

    @Override
    public int evaluate(int x) {
        return intValue;
    }

    @Override
    public double evaluate(double x) {
        return doubleValue;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return intValue;
    }

    @Override
    public String toString() {
        return initializedInt ? String.valueOf(intValue) : String.valueOf(doubleValue);
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return initializedInt ? intValue == ((Const) o).intValue : Double.compare(doubleValue, ((Const) o).doubleValue) == 0;
    }

    @Override
    public int hashCode() {
        return intValue + Double.hashCode(doubleValue);
    }
}
