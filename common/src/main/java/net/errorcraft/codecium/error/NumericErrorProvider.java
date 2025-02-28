package net.errorcraft.codecium.error;

import java.util.function.Supplier;

public class NumericErrorProvider {
    private NumericErrorProvider() {}

    public static <N extends Number> Supplier<String> atLeast(String name, N min, N value) {
        return () -> name + " must be at least " + min + ": " + value;
    }

    public static <N extends Number> Supplier<String> atMost(String name, N max, N value) {
        return () -> name + " must be at most " + max + ": " + value;
    }
}
