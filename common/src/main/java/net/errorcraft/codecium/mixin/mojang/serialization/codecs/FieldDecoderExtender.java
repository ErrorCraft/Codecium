package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.FieldDecoder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = FieldDecoder.class, remap = false)
public class FieldDecoderExtender {
    @Shadow
    @Final
    protected String name;

    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private <T> Supplier<String> useBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final MapLike<T> input) {
        return () -> "Key '" + this.name +  "' must be present in map: " + input;
    }
}
