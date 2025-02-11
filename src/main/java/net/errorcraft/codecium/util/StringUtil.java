package net.errorcraft.codecium.util;

import java.util.Objects;
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
}
