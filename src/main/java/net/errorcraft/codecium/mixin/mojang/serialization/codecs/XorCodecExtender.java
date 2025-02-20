package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.XorCodec;
import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(value = XorCodec.class, remap = false)
public class XorCodecExtender<F, S> {
    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    @SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent" })
    private <T> Supplier<String> useBetterErrorMessage(Supplier<String> message, @Local(name = "firstResult") final Optional<Pair<Either<F, S>, T>> firstResult, @Local(name = "secondResult") final Optional<Pair<Either<F, S>, T>> secondResult) {
        return () -> "Decoded both alternatives successfully, cannot pick the correct one:\n" + StringUtil.indent(
            "1: " + firstResult.get() + "\n" +
            "2: " + secondResult.get()
        );
    }

    @ModifyReturnValue(
        method = "decode",
        at = @At("TAIL")
    )
    private <T> DataResult<Pair<Either<F, S>, T>> useBetterErrorMessage(DataResult<Pair<Either<F, S>, T>> original, @Local(name = "firstRead") final DataResult<Pair<Either<F, S>, T>> firstRead, @Local(name = "secondRead") final DataResult<Pair<Either<F, S>, T>> secondRead) {
        return DataResult.error(StringUtil.supplyEitherErrorMessage(firstRead, secondRead));
    }
}
