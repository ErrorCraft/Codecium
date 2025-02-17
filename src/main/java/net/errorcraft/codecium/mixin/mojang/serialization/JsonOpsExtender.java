package net.errorcraft.codecium.mixin.mojang.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import net.errorcraft.codecium.util.codec.ExceptionUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
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
        method = "getNumberValue(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 1
        )
    )
    private Supplier<String> notANumberUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Element is not a number: " + input;
    }

    @ModifyArg(
        method = "getBooleanValue(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notABooleanUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Element is not a boolean: " + input;
    }

    @ModifyArg(
        method = "getStringValue(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAStringUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Element is not a string: " + input;
    }

    @ModifyArg(
        method = { "getMapValues(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getMapEntries(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getMap(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAMapUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Element is not a map: " + input;
    }

    @ModifyArg(
        method = { "getStream(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;", "getList(Lcom/google/gson/JsonElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final JsonElement input) {
        return () -> "Element is not a list: " + input;
    }

    @Mixin(targets = "com/mojang/serialization/JsonOps$1")
    public static class MapLikeExtender<T> {
        @Shadow
        @Final
        JsonObject val$object;

        /**
         * @author ErrorCraft
         * @reason Uses the element itself instead of a wrapped element.
         */
        @Overwrite
        public String toString() {
            return this.val$object.toString();
        }
    }
}
