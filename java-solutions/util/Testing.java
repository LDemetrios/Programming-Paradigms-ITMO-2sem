package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class Testing {
    private static int failed = 0;

    private static int indentLevel = 0;

    public static String indent() {
        return "    ".repeat(indentLevel);
    }

    public static void println(Object x) {
        print(x.toString() + "\n");
    }

    public static void println() {
        print("\n");
    }

    public static void print(Object x) {
        System.out.print(x.toString().replaceAll("\r\n|[\r\n]", "\n" + indent()));
    }

    public static void chapter(String header) {
        indentLevel++;
        println(header + ":");
    }

    public static void endChapter() {
        indentLevel--;
        println();
    }

    public static void check(String testName, Object expected, Object result) {
        if (Objects.equals(expected, result)) {
            println("Success " + testName);
        } else {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            println("Failed %s: %s != %s (line %d)".formatted(testName, expected, result, trace[2].getLineNumber()));
            failed++;
        }
    }

    public static void check(String testName, ThrowableSupplier<Object> expected, Object result) {
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

        try {
            Object exp = expected.get();
            if (Objects.equals(exp, result)) {
                println("Success " + testName);
            } else {
                println("Failed %s: %s != %s (line %d)".formatted(testName, expected, result, lineNumber));
                failed++;
            }
        } catch (Exception | AssertionError e) {
            println("Failed %s (line %d), threw:".formatted(testName, lineNumber));
            e.printStackTrace(System.out);
        } catch (Throwable t) {
            sneakyThrow(t);
        }

    }

    public static void finish() {
        while (indentLevel > 0) {
            endChapter();
        }
        println("Failed " + failed + " tests");
    }

    public static ThrowableSupplier<Object> call(Object obj, String methodName, Object... args) {
        Method method;
        try {
            method = obj.getClass().getMethod(
                    methodName,
                    Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)
            );
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Incorrect method specified", e);
        }
        return () -> {
            try {
                return method.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        };
    }

    public interface ThrowableSupplier<T> {
        T get() throws Throwable;
    }

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        //noinspection unchecked
        throw (E) e;
    }
}
