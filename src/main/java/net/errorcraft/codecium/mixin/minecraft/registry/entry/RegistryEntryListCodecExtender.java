package net.errorcraft.codecium.mixin.minecraft.registry.entry;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryListCodec;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(RegistryEntryListCodec.class)
public class RegistryEntryListCodecExtender<E> {
    @Shadow
    @Final
    private RegistryKey<? extends Registry<E>> registry;

    @Unique
    private static RegistryKey<? extends Registry<?>> tempRegistryKey;

    @ModifyArg(
        method = "method_58027",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <E> Supplier<String> unknownRegistryTagUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) TagKey<E> tag) {
        return () -> "Cannot get a registry tag with id " + tag.id();
    }

    @ModifyArg(
        method = "encode(Lnet/minecraft/registry/entry/RegistryEntryList;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> invalidOwnerUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) RegistryEntryList<E> registryEntries) {
        return () -> "Registry tag " + registryEntries.getTagKey().orElseThrow().id() + " is not part of the current registry set";
    }

    @Inject(
        method = "decodeDirect",
        at = @At("HEAD")
    )
    private <T> void storeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<RegistryEntryList<E>, T>>> info) {
        RegistryEntryListCodecExtender.tempRegistryKey = this.registry;
    }

    @ModifyArg(
        method = "method_40381",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <R> Supplier<String> inaccessibleRegistryUseBetterErrorMessage(Supplier<String> message, @Local RegistryEntry<R> entry) {
        return () -> "Registry " + RegistryEntryListCodecExtender.tempRegistryKey.getValue() + " is inaccessible for " + entry.getKey().orElseThrow();
    }

    @Inject(
        method = "decodeDirect",
        at = @At("TAIL")
    )
    private <T> void removeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<RegistryEntryList<E>, T>>> info) {
        RegistryEntryListCodecExtender.tempRegistryKey = null;
    }
}
