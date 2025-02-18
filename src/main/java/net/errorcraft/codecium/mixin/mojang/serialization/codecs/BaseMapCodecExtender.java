package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.BaseMapCodec;
import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Mixin(value = BaseMapCodec.class, remap = false)
public interface BaseMapCodecExtender<K, V> {
    @Shadow
    Codec<K> keyCodec();

    @ModifyArg(
        method = "lambda$decode$3",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    @SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent" })
    private <T> Supplier<String> duplicateFieldUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final DynamicOps<T> ops, @Local(argsOnly = true) final Pair<T, T> pair, @Local final Optional<Pair<K, V>> entry) {
        return () -> "Duplicate field " + this.keyCodec().encodeStart(ops, entry.get().getFirst()).getOrThrow() + ": " + pair.getFirst();
    }

    @WrapOperation(
        method = "lambda$decode$3",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0
        )
    )
    private <T> DataResult<K> addKeyToKeyErrorMessage(Codec<K> instance, final DynamicOps<T> dynamicOps, Object o, Operation<DataResult<K>> original, @Local(argsOnly = true) final Pair<T, T> pair) {
        return original.call(instance, dynamicOps, o)
            .mapError(message -> "For key " + pair.getFirst() + ": " + message);
    }

    @ModifyArg(
        method = "lambda$decode$3",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;apply2stable(Ljava/util/function/BiFunction;Lcom/mojang/serialization/DataResult;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0
        )
    )
    private <T> DataResult<V> addKeyToValueErrorMessage(DataResult<V> second, @Local(argsOnly = true) final Pair<T, T> pair) {
        return second.mapError(message -> "For value " + pair.getFirst() + ": " + message);
    }

    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;mapError(Ljava/util/function/UnaryOperator;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private <T> UnaryOperator<String> mapErrorsUseBetterErrorMessage(UnaryOperator<String> function, @Local final T errors) {
        return message -> "Map has errors:\n" + StringUtil.indent(message);
    }
}
