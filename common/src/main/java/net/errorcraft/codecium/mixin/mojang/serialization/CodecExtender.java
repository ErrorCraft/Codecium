package net.errorcraft.codecium.mixin.mojang.serialization;

import com.mojang.serialization.*;
import net.errorcraft.codecium.serialization.FieldMapCodec;
import net.errorcraft.codecium.util.EnglishUtil;
import net.errorcraft.codecium.util.codec.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = Codec.class, remap = false)
public interface CodecExtender<A> {
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
                return DataResult.error(() -> "String must have at least " + minSize + " " + EnglishUtil.pluralize(minSize, "character") + ", but got " + length + ": \"" + value + "\"");
            }
            if (length > maxSize) {
                return DataResult.error(() -> "String must have at most " + maxSize + " " + EnglishUtil.pluralize(minSize, "character") + ", but got " + length + ": \"" + value + "\"");
            }
            return DataResult.success(value);
        });
    }

    @Redirect(
        method = "fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/MapCodec;of(Lcom/mojang/serialization/MapEncoder;Lcom/mojang/serialization/MapDecoder;Ljava/util/function/Supplier;)Lcom/mojang/serialization/MapCodec;"
        )
    )
    private MapCodec<A> createFieldCodecPassName(MapEncoder<A> encoder, MapDecoder<A> decoder, Supplier<String> nameSupplier, final String name) {
        return new FieldMapCodec<>(name, encoder, decoder, nameSupplier);
    }
}
