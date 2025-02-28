package net.errorcraft.codecium.mixin.minecraft.util.dynamic;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.ExtraCodecs;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(ExtraCodecs.class)
public class ExtraCodecsExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ExtraCodecs;intRangeWithMessage(IILjava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/ExtraCodecs;UNSIGNED_BYTE:Lcom/mojang/serialization/Codec;",
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
            target = "Lnet/minecraft/util/ExtraCodecs;intRangeWithMessage(IILjava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/util/ExtraCodecs;NON_NEGATIVE_INT:Lcom/mojang/serialization/Codec;",
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
            target = "Lnet/minecraft/util/ExtraCodecs;floatRangeMinInclusiveWithMessage(FFLjava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Function<Float, String> nonNegativeFloat(Function<Float, String> messageFactory) {
        return value -> "Float must be non-negative: " + value;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ExtraCodecs;floatRangeMinExclusiveWithMessage(FFLjava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Function<Float, String> positiveFloat(Function<Float, String> messageFactory) {
        return value -> "Float must be positive: " + value;
    }
}
