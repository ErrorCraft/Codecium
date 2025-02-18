package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.EitherCodec;
import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;
import java.util.function.Supplier;

@Mixin(value = EitherCodec.class, remap = false)
public class EitherCodecExtender<F, S> {
    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private <T> Supplier<String> useBetterErrorMessage(Supplier<String> message, @Local(name = "firstRead") final DataResult<Pair<Either<F, S>, T>> firstRead, @Local(name = "secondRead") final DataResult<Pair<Either<F, S>, T>> secondRead) {
        return () -> {
            String first = firstRead.error().orElseThrow().message();
            String second = secondRead.error().orElseThrow().message();
            if (Objects.equals(first, second)) {
                return first;
            }
            return "Failed to decode either:\n" + StringUtil.indent(
                "1: " + first + "\n" +
                "2: " + second
            );
        };
    }
}
