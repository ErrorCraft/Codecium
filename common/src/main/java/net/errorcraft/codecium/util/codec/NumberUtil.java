package net.errorcraft.codecium.util.codec;

import com.mojang.serialization.DataResult;
import net.errorcraft.codecium.error.NumericErrorProvider;

import java.util.function.Function;

public class NumberUtil {
    private NumberUtil() {}

    public static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(final N minInclusive, final N maxInclusive, String name) {
        return value -> {
            if (value.compareTo(minInclusive) < 0) {
                return DataResult.error(NumericErrorProvider.atLeast(name, minInclusive, value));
            }
            if (value.compareTo(maxInclusive) > 0) {
                return DataResult.error(NumericErrorProvider.atMost(name, maxInclusive, value));
            }
            return DataResult.success(value);
        };
    }
}
