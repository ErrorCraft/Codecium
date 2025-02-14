package net.errorcraft.codecium.mixin.minecraft.util.dynamic;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.dynamic.Codecs;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(Codecs.class)
public class CodecsExtender {
    @ModifyArg(
        method = "method_56907",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> unsignedByteTooLarge(Supplier<String> message, @Local(argsOnly = true) final Integer value) {
        return () -> "Unsigned byte must be at most 255: " + value;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/dynamic/Codecs;rangedInt(IILjava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/dynamic/Codecs;UNSIGNED_BYTE:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.PUTSTATIC
            )
        )
    )
    private static Function<Integer, String> nonNegativeInteger(Function<Integer, String> messageFactory) {
        return value -> "Integer must be non-negative: " + value;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/dynamic/Codecs;rangedInt(IILjava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/dynamic/Codecs;NON_NEGATIVE_INT:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.PUTSTATIC
            )
        )
    )
    private static Function<Integer, String> positiveInteger(Function<Integer, String> messageFactory) {
        return value -> "Integer must be positive: " + value;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/dynamic/Codecs;rangedInclusiveFloat(FFLjava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Function<Float, String> nonNegativeFloat(Function<Float, String> messageFactory) {
        return value -> "Float must be non-negative: " + value;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/dynamic/Codecs;rangedFloat(FFLjava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Function<Float, String> positiveFloat(Function<Float, String> messageFactory) {
        return value -> "Float must be positive: " + value;
    }
}
