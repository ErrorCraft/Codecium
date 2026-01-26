package net.errorcraft.codecium.mixin.minecraft.nbt;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(NbtOps.class)
public class NbtOpsExtender {
    @ModifyArg(
        method = "getNumberValue(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;orElseGet(Ljava/util/function/Supplier;)Ljava/lang/Object;"
        )
    )
    private Supplier<DataResult<Number>> notANumberUseBetterErrorMessage(Supplier<DataResult<Number>> supplier, @Local(argsOnly = true) final Tag input) {
        return () -> DataResult.error(() -> "Element is not a number: " + input);
    }

    @ModifyArg(
        method = "getStringValue(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAStringUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Tag input) {
        return () -> "Element is not a string: " + input;
    }

    @ModifyArg(
        method = { "getMapValues(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;", "getMapEntries(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;", "getMap(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAMapUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Tag input) {
        return () -> "Element is not a map: " + input;
    }

    @ModifyArg(
        method = { "getStream(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;", "getList(Lnet/minecraft/nbt/Tag;)Lcom/mojang/serialization/DataResult;" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Tag input) {
        return () -> "Element is not a list: " + input;
    }

    @Mixin(targets = "net/minecraft/nbt/NbtOps$1")
    public static class MapLikeExtender {
        @Shadow
        @Final
        CompoundTag val$tag;

        /**
         * @author ErrorCraft
         * @reason Uses the element itself instead of a wrapped element.
         */
        @Overwrite
        public String toString() {
            return this.val$tag.toString();
        }
    }
}
