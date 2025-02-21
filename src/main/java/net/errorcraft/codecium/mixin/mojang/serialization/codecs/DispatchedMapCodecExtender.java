package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.DispatchedMapCodec;
import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Mixin(value = DispatchedMapCodec.class, remap = false)
public class DispatchedMapCodecExtender<K, V> {
    @Shadow
    @Final
    private Codec<K> keyCodec;

    @ModifyArg(
        method = "parseEntry",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    @SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent" })
    private <T> Supplier<String> duplicateFieldUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final DynamicOps<T> ops, @Local(argsOnly = true) final Pair<T, T> pair, @Local final Optional<Pair<K, V>> entry) {
        return () -> "Duplicate field " + this.keyCodec.encodeStart(ops, entry.get().getFirst()).getOrThrow() + ": " + pair.getFirst();
    }

    @ModifyReturnValue(
        method = "parseEntry",
        at = @At("TAIL")
    )
    private <T> DataResult<Unit> duplicateFieldUseBetterErrorMessage(DataResult<Unit> original, @Local(argsOnly = true) final Pair<T, T> input) {
        return original.mapError(message -> "For field " + input.getFirst() + ": " + message);
    }

    @ModifyArg(
        method = "lambda$decode$5",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;mapError(Ljava/util/function/UnaryOperator;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private UnaryOperator<String> mapErrorsUseBetterErrorMessage(UnaryOperator<String> function) {
        return message -> "Map has errors:\n" + StringUtil.indent(message);
    }
}
