package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.ListCodec;
import net.errorcraft.codecium.util.EnglishUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = ListCodec.class, remap = false)
public class ListCodecExtender<E> {
    @Shadow
    @Final
    private int minSize;

    @Shadow
    @Final
    private int maxSize;

    @ModifyArg(
        method = "createTooShortError",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> tooShortUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final int size) {
        return () -> "List must have at least " + this.minSize + " " + EnglishUtil.pluralize(this.minSize, "value") + ", but got " + size;
    }

    @ModifyArg(
        method = "createTooLongError",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> tooLongUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final int size) {
        return () -> "List must have at most " + this.maxSize + " " + EnglishUtil.pluralize(this.maxSize, "value") + ", but got " + size;
    }

    @ModifyReturnValue(
        method = "decode",
        at = @At("TAIL")
    )
    private <T> DataResult<Pair<List<E>, T>> listErrorAddListToErrorMessage(DataResult<Pair<List<E>, T>> original, @Local(argsOnly = true) final T input) {
        return original.mapError(message -> message + ": " + input);
    }
}
