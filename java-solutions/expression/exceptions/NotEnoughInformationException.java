package expression.exceptions;

import util.Utils;

public class NotEnoughInformationException extends EvaluationException {
    public NotEnoughInformationException() {
    }

    public NotEnoughInformationException(String s) {
        super(s);
    }

    public static NotEnoughInformationException forVariable(Object variable, Object... given) {
        return new NotEnoughInformationException(
                "Not enough information to evaluate Variable[%s], only %s %s given"
                        .formatted(
                                variable,
                                Utils.naturallyJoin((Object[]) zip(given)),
                                given.length == 1 ? "is" : "are"
                        )
        );
    }

    private static String[] zip(Object[] given) {
        final int lim = given.length >> 1;
        if ((given.length & 1) != 0) {
            throw new IllegalArgumentException("Illegal use of .forVariable(Object, Object...): length `Object... given` must be even.");
        }
        String[] res = new String[lim];
        for (int i = 0; i < lim; i++) {
            res[i] = given[i * 2] + " = " + given[i * 2 + 1];
        }
        return res;
    }
}
