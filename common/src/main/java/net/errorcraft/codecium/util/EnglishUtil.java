package net.errorcraft.codecium.util;

public class EnglishUtil {
    private EnglishUtil() {}

    public static String pluralize(int amount, String singular) {
        return pluralize(amount, singular, singular + "s");
    }

    public static String pluralize(int amount, String singular, String plural) {
        return amount == 1 ? singular : plural;
    }
}
