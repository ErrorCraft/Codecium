package net.errorcraft.codecium.mixin.neoforge.minecraft.nbt;

import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

public class NbtOpsExtender {
    @Mixin(targets = "net/minecraft/nbt/NbtOps$1")
    public static class MapLikeExtender {
        @Shadow
        @Final
        CompoundTag val$compoundtag;

        /**
         * @author ErrorCraft
         * @reason Uses the element itself instead of a wrapped element.
         */
        @Overwrite
        public String toString() {
            return this.val$compoundtag.toString();
        }
    }
}
