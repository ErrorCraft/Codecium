package net.errorcraft.codecium.mixin.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.HolderSetCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecExtender<E> {
    @ModifyArg(
        method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) HolderSet<E> holders) {
        return () -> "Registry tag " + holders.unwrapKey().orElseThrow().location() + " is not part of the current registry set";
    }
}
