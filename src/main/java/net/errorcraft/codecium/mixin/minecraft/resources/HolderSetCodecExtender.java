package net.errorcraft.codecium.mixin.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecExtender<E> {
    @Shadow
    @Final
    private ResourceKey<? extends Registry<E>> registryKey;

    @Unique
    private static ResourceKey<? extends Registry<?>> tempRegistryKey;

    @ModifyArg(
        method = "method_58027",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <E> Supplier<String> unknownRegistryTagUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) TagKey<E> tag) {
        return () -> "Cannot get a registry tag with id " + tag.location();
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) HolderSet<E> registryEntries) {
        return () -> "Registry tag " + registryEntries.unwrapKey().orElseThrow().location() + " is not part of the current registry set";
    }

    @Inject(
        method = "decodeWithoutRegistry",
        at = @At("HEAD")
    )
    private <T> void storeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<HolderSet<E>, T>>> info) {
        HolderSetCodecExtender.tempRegistryKey = this.registryKey;
    }

    @ModifyArg(
        method = "method_40381",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <R> Supplier<String> inaccessibleRegistryUseBetterErrorMessage(Supplier<String> message, @Local Holder<R> entry) {
        return () -> "Registry " + HolderSetCodecExtender.tempRegistryKey.location() + " is inaccessible for " + entry.unwrapKey().orElseThrow();
    }

    @Inject(
        method = "decodeWithoutRegistry",
        at = @At("TAIL")
    )
    private <T> void removeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<HolderSet<E>, T>>> info) {
        HolderSetCodecExtender.tempRegistryKey = null;
    }
}
