package net.errorcraft.codecium.mixin.mojang.serialization;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JavaOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = JavaOps.class, remap = false)
public class JavaOpsExtender {
    @ModifyArg(
        method = "getNumberValue",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notANumberUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Object input) {
        return () -> "Element is not a number: " + input;
    }

    @ModifyArg(
        method = "getBooleanValue",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notABooleanUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Object input) {
        return () -> "Element is not a boolean: " + input;
    }

    @ModifyArg(
        method = "getStringValue",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notAStringUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Object input) {
        return () -> "Element is not a string: " + input;
    }

    @ModifyArg(
        method = { "mergeToMap(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", "mergeToMap(Ljava/lang/Object;Ljava/util/Map;)Lcom/mojang/serialization/DataResult;", "mergeToMap(Ljava/lang/Object;Lcom/mojang/serialization/MapLike;)Lcom/mojang/serialization/DataResult;", "getMapValues", "getMapEntries(Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", "getMap" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notAMapUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, ordinal = 0) final Object input) {
        return () -> "Element is not a map: " + input;
    }

    @ModifyArg(
        method = { "mergeToList(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", "mergeToList(Ljava/lang/Object;Ljava/util/List;)Lcom/mojang/serialization/DataResult;", "getStream", "getList", "getByteBuffer", "getIntStream", "getLongStream" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, ordinal = 0) final Object input) {
        return () -> "Element is not a list: " + input;
    }
}
