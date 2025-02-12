package net.errorcraft.codecium.mixin.mojang.serialization;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import net.errorcraft.codecium.util.codec.ExceptionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = JsonOps.class, remap = false)
public class JsonOpsExtender {
    @ModifyArg(
        method = "getNumberValue(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0
        )
    )
    private Supplier<String> notANumberWithExceptionUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input, @Local final NumberFormatException e) {
        return () -> ExceptionUtil.errorMessage(e, input);
    }

    @ModifyArg(
        method = { "getMapValues(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getMapEntries(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getMap(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAMapUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Not a map: " + input;
    }

    @ModifyArg(
        method = { "getStream(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getList(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Not a list: " + input;
    }
}
