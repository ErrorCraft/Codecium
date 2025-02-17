package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.DataResult;
import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class RecordCodecBuilderExtender {
    @Mixin(targets = "com/mojang/serialization/codecs/RecordCodecBuilder$2", remap = false)
    public static class RecordMapCodecExtender<O> {
        @ModifyReturnValue(
            method = "decode",
            at = @At("RETURN")
        )
        private DataResult<O> useBetterErrorMessage(DataResult<O> original) {
            return original.mapError(message -> "Map has errors for the following fields:\n" + StringUtil.indent(message));
        }
    }
}
