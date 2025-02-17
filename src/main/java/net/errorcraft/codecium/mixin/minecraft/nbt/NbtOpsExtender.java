package net.errorcraft.codecium.mixin.minecraft.nbt;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(NbtOps.class)
public class NbtOpsExtender {
    @ModifyArg(
        method = "getNumberValue(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notANumberUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final NbtElement input) {
        return () -> "Element is not a number: " + input;
    }

    @ModifyArg(
        method = "getStringValue(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notAStringUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final NbtElement input) {
        return () -> "Element is not a string: " + input;
    }

    @ModifyArg(
        method = { "getMapValues(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;", "getMapEntries(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;", "getMap(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notAMapUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final NbtElement input) {
        return () -> "Element is not a map: " + input;
    }

    @ModifyArg(
        method = { "getStream(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;", "getList(Lnet/minecraft/nbt/NbtElement;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final NbtElement input) {
        return () -> "Element is not a list: " + input;
    }
}
