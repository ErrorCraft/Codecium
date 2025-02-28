package net.errorcraft.codecium.mixin.fabric.minecraft.util.dynamic;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.codecium.error.NumericErrorProvider;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(ExtraCodecs.class)
public class ExtraCodecsExtender {
    @ModifyArg(
        method = "method_56907",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> unsignedByteTooLargeUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Integer value) {
        return NumericErrorProvider.atMost("Unsigned byte", 255, value);
    }
}
