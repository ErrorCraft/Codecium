package net.errorcraft.codecium.mixin.neoforge.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.codecium.error.RegistryErrorProvider;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(RegistryFixedCodec.class)
public class RegistryFixedCodecExtender {
    @ModifyArg(
        method = "lambda$encode$3",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> directHolderUseBetterErrorMessage(Supplier<String> message) {
        return RegistryErrorProvider.noDirectHolder();
    }

    @ModifyArg(
        method = "lambda$decode$6",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> unknownHolderUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) ResourceLocation id) {
        return RegistryErrorProvider.unknownHolder(id);
    }
}
