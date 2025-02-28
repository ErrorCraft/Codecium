package net.errorcraft.codecium.mixin.fabric.minecraft.resources;

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
        method = "method_40397",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> directRegistryEntryUseBetterErrorMessage(Supplier<String> message) {
        return RegistryErrorProvider.noDirectHolder();
    }

    @ModifyArg(
        method = "method_46625",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> unknownRegistryEntryUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) ResourceLocation id) {
        return RegistryErrorProvider.unknownHolder(id);
    }
}
