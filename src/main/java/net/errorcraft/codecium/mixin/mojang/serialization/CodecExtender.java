package net.errorcraft.codecium.mixin.mojang.serialization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.codecium.util.codec.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(value = Codec.class, remap = false)
public interface CodecExtender {
    @Redirect(
        method = "intRange",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;checkRange(Ljava/lang/Number;Ljava/lang/Number;)Ljava/util/function/Function;"
        )
    )
    private static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRangeIntegerUseBetterCheck(N minInclusive, N maxInclusive) {
        return NumberUtil.checkRange(minInclusive, maxInclusive, "Integer");
    }

    @Redirect(
        method = "floatRange",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;checkRange(Ljava/lang/Number;Ljava/lang/Number;)Ljava/util/function/Function;"
        )
    )
    private static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRangeFloatUseBetterCheck(N minInclusive, N maxInclusive) {
        return NumberUtil.checkRange(minInclusive, maxInclusive, "Float");
    }

    @Redirect(
        method = "doubleRange",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;checkRange(Ljava/lang/Number;Ljava/lang/Number;)Ljava/util/function/Function;"
        )
    )
    private static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRangeDoubleUseBetterCheck(N minInclusive, N maxInclusive) {
        return NumberUtil.checkRange(minInclusive, maxInclusive, "Double");
    }

    /**
     * @author ErrorCraft
     * @reason I do not want to use all the generated lambda names, and I am replacing it unconditionally anyways.
     */
    @Overwrite
    static Codec<String> string(final int minSize, final int maxSize) {
        return Codec.STRING.validate(value -> {
            final int length = value.length();
            if (length < minSize) {
                return DataResult.error(() -> "String must have at least " + minSize + " characters, but got " + length + ": \"" + value + "\"");
            }
            if (length > maxSize) {
                return DataResult.error(() -> "String must have at most " + maxSize + " characters, but got " + length + ": \"" + value + "\"");
            }
            return DataResult.success(value);
        });
    }
}
