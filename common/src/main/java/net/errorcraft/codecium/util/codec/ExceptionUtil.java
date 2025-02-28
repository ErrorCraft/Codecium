package net.errorcraft.codecium.util.codec;

public class ExceptionUtil {
    private ExceptionUtil() {}

    public static <T> String errorMessage(Throwable throwable, T input) {
        return throwable.getMessage() + "\n  Element: " + input;
    }
}
