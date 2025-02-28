package net.errorcraft.codecium.mixin.fabric.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.codecium.error.RegistryErrorProvider;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(RegistryFileCodec.class)
public class RegistryFileCodecExtender {
    @ModifyArg(
        method = "method_46624",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <E> Supplier<String> unknownHolderUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) ResourceKey<E> key) {
        return RegistryErrorProvider.unknownHolder(key.location());
    }
}
