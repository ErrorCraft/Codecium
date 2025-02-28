package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = KeyDispatchCodec.class, remap = false)
public class KeyDispatchCodecExtender {
    @Shadow
    @Final
    private static String COMPRESSED_VALUE_KEY;

    @Shadow
    @Final
    private String typeKey;

    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private <T> Supplier<String> noTypeKeyUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final MapLike<T> input) {
        return () -> "Key '" + this.typeKey + "' must be present in map: " + input;
    }

    @ModifyArg(
        method = "lambda$decode$3",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static <T> Supplier<String> noValueKeyUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final MapLike<T> input) {
        return () -> "Key '" + COMPRESSED_VALUE_KEY + "' must be present in map: " + input;
    }
}
