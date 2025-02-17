package net.errorcraft.codecium.mixin.mojang.serialization.codecs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = OptionalFieldCodec.class, remap = false)
public class OptionalFieldCodecExtender<A> {
    @Shadow
    @Final
    private String name;

    @ModifyReturnValue(
        method = "decode",
        at = @At("TAIL")
    )
    private DataResult<Optional<A>> modifyPotentialErrorMessage(DataResult<Optional<A>> original) {
        return original.mapError(message -> "'" + this.name + "': " + message);
    }
}
