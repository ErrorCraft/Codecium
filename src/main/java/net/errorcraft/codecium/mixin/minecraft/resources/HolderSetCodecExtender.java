package net.errorcraft.codecium.mixin.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecExtender<E> {
    @Shadow
    @Final
    private ResourceKey<? extends Registry<E>> registryKey;

    @ModifyArg(
        method = "lambda$decode$1",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> decodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registryKey.identifier() + " is inaccessible";
    }

    @ModifyArg(
        method = "lambda$lookupTag$0",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static <E> Supplier<String> unknownRegistryTagUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, name = "key") TagKey<E> key) {
        return () -> "Cannot get a registry tag with id " + key.location();
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0
        )
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true, name = "input") HolderSet<E> input) {
        return () -> "Registry tag " + input.unwrapKey().orElseThrow().location() + " is not part of the current registry set";
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 1
        )
    )
    private Supplier<String> encodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registryKey.identifier() + " is inaccessible";
    }
}
