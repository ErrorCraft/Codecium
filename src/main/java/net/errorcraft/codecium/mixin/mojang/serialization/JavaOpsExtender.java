package net.errorcraft.codecium.mixin.mojang.serialization;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JavaOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = JavaOps.class, remap = false)
public class JavaOpsExtender {
    @ModifyArg(
        method = { "getStream", "getList", "getByteBuffer", "getIntStream", "getLongStream" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private Supplier<String> notAListUseBetterErrorMessage(Supplier<String> message, @Local(argsOnly = true) final Object input) {
        return () -> "Not a list: " + input;
    }
}
