package net.errorcraft.codecium.mixin.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(RegistryFixedCodec.class)
public class RegistryFixedCodecExtender<E> {
    @Shadow
    @Final
    private ResourceKey<? extends Registry<E>> registryKey;

    @ModifyArg(
        method = "encode(Lnet/minecraft/core/Holder;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0
        )
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, name = "input") Holder<E> input) {
        return () -> "Holder " + input.unwrapKey().orElseThrow().identifier() + " is not part of the current registry set";
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/core/Holder;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 1
        )
    )
    private Supplier<String> encodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registryKey.identifier() + " is inaccessible";
    }

    @ModifyArg(
        method = "lambda$encode$2",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> directHolderUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Cannot encode a direct holder";
    }

    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> decodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registryKey.identifier() + " is inaccessible";
    }

    @ModifyArg(
        method = "lambda$decode$1",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static Supplier<String> unknownHolderUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, name = "id") Identifier id) {
        return () -> "Cannot get a holder with id " + id;
    }
}
