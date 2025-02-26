package net.errorcraft.codecium.mixin.minecraft.registry.entry;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Identifier;
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
    private RegistryKey<? extends Registry<E>> registry;

    @ModifyArg(
        method = "encode(Lnet/minecraft/registry/entry/RegistryEntry;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 0,
            remap = false
        )
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) RegistryEntry<E> registryEntry) {
        return () -> "Holder " + registryEntry.getKey().orElseThrow().getValue() + " is not part of the current registry set";
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/registry/entry/RegistryEntry;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            ordinal = 1,
            remap = false
        )
    )
    private Supplier<String> encodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registry.getValue() + " is inaccessible";
    }

    @ModifyArg(
        method = "method_40397",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> directRegistryEntryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Cannot encode a direct holder";
    }

    @ModifyArg(
        method = "decode",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> decodeInaccessibleRegistryUseBetterErrorMessage(Supplier<String> message) {
        return () -> "Registry " + this.registry.getValue() + " is inaccessible";
    }

    @ModifyArg(
        method = "method_46625",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> unknownRegistryEntryUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) Identifier id) {
        return () -> "Cannot get a registry entry with id " + id;
    }
}
