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

@Mixin(KeyDispatchCodec.class)
public class KeyDispatchCodecExtender {
    @Shadow
    @Final
    private static String COMPRESSED_VALUE_KEY;

    @ModifyArg(
        method = "lambda$decode$2",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static <T> Supplier<String> noValueKeyUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, name = "input") final MapLike<T> input) {
        return () -> "Key '" + COMPRESSED_VALUE_KEY + "' must be present in map: " + input;
    }
}
