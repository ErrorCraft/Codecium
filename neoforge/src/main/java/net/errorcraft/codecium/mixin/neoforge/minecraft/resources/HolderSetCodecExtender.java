package net.errorcraft.codecium.mixin.neoforge.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.codecium.error.RegistryErrorProvider;
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
    private static ResourceKey<? extends Registry<?>> codecium$tempRegistryKey;

    @ModifyArg(
        method = { "lambda$lookupTag$7" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <E> Supplier<String> unknownRegistryTagUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) TagKey<E> tag) {
        return RegistryErrorProvider.unknownTag(tag);
    }

    @Inject(
        method = "decodeWithoutRegistry",
        at = @At("HEAD")
    )
    private <T> void storeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<HolderSet<E>, T>>> info) {
        HolderSetCodecExtender.codecium$tempRegistryKey = this.registryKey;
    }

    @ModifyArg(
        method = "lambda$decodeWithoutRegistry$10",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <R> Supplier<String> inaccessibleRegistryUseBetterErrorMessage(Supplier<String> message, @Local Holder<R> holder) {
        return RegistryErrorProvider.inaccessibleRegistry(HolderSetCodecExtender.codecium$tempRegistryKey, holder);
    }

    @Inject(
        method = "decodeWithoutRegistry",
        at = @At("TAIL")
    )
    private <T> void removeTemporaryRegistryKey(DynamicOps<T> ops, T input, CallbackInfoReturnable<DataResult<Pair<HolderSet<E>, T>>> info) {
        HolderSetCodecExtender.codecium$tempRegistryKey = null;
    }
}
