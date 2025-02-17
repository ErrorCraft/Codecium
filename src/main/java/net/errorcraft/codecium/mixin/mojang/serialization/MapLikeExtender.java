package net.errorcraft.codecium.mixin.mojang.serialization;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

public interface MapLikeExtender {
    @Mixin(targets = "com/mojang/serialization/MapLike$1", remap = false)
    class ForMapExtender<T> {
        @Shadow
        @Final
        Map<T, T> val$map;

        /**
         * @author ErrorCraft
         * @reason Uses the element itself instead of a wrapped element.
         */
        @Overwrite
        public String toString() {
            return this.val$map.toString();
        }
    }
}
