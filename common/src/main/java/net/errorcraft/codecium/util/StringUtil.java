package net.errorcraft.codecium.util;

import com.mojang.serialization.DataResult;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StringUtil {
    private static final String DEFAULT_INDENTATION = "  ";

    private StringUtil() {}

    public static String indent(String value) {
        return indent(value, DEFAULT_INDENTATION);
    }

    public static String indent(String value, String indentation) {
        return indent(value.lines(), indentation);
    }

    private static String indent(Stream<String> lines, String indentation) {
        Objects.requireNonNull(lines);
        Objects.requireNonNull(indentation);
        StringBuilder builder = new StringBuilder();
        lines.forEach(line -> {
            builder.append(indentation);
            builder.append(line);
            builder.append('\n');
        });
        if (builder.isEmpty()) {
            return "";
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static <T> Supplier<String> supplyEitherErrorMessage(final DataResult<T> first, final DataResult<T> second) {
        return () -> {
            String firstMessage = first.error().orElseThrow().message();
            String secondMessage = second.error().orElseThrow().message();
            if (Objects.equals(firstMessage, secondMessage)) {
                return firstMessage;
            }
            return "Failed to decode either:\n" + indent(
                "1: " + firstMessage + "\n" +
                "2: " + secondMessage
            );
        };
    }
}
