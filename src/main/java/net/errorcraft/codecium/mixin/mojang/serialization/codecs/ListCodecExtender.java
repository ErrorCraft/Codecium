package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.ListCodec;
import net.errorcraft.codecium.access.mojang.serialization.codecs.ListCodecAccess;
import net.errorcraft.codecium.util.EnglishUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = ListCodec.class, remap = false)
public class ListCodecExtender<E> implements ListCodecAccess {
    @Shadow
    @Final
    private int minSize;

    @Shadow
    @Final
    private int maxSize;

    @Unique
    private Object temporaryInput;

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

    @Inject(
        method = "decode",
        at = @At("HEAD")
    )
    private <T> void storeTemporaryInput(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<List<E>, T>>> info) {
        this.temporaryInput = input;
    }

    @Override
    public Object codecium$getAndRemoveTemporaryInput() {
        Object input = this.temporaryInput;
        this.temporaryInput = null;
        return input;
    }

    @Mixin(targets = "com/mojang/serialization/codecs/ListCodec$DecoderState", remap = false)
    public static class DecoderStateExtender<T> {
        @Shadow
        @Final
        ListCodec<T> this$0;

        @ModifyExpressionValue(
            method = "build",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/ListCodec;createTooShortError(I)Lcom/mojang/serialization/DataResult;"
            )
        )
        private <R> DataResult<R> tooShortAddListToErrorMessage(DataResult<R> original) {
            return original.mapError(message -> message + ": " + ((ListCodecAccess)(Object) this.this$0).codecium$getAndRemoveTemporaryInput());
        }

        @ModifyExpressionValue(
            method = "build",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/ListCodec;createTooLongError(I)Lcom/mojang/serialization/DataResult;"
            )
        )
        private <R> DataResult<R> tooLongAddListToErrorMessage(DataResult<R> original) {
            return original.mapError(message -> message + ": " + ((ListCodecAccess)(Object) this.this$0).codecium$getAndRemoveTemporaryInput());
        }
    }
}
