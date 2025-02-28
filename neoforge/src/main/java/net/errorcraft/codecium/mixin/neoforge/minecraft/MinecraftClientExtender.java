package net.errorcraft.codecium.mixin.neoforge.minecraft;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientExtender {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        System.out.println("HELLO APPLIED MIXINS");
    }
}
