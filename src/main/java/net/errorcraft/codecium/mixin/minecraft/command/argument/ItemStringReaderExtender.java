package net.errorcraft.codecium.mixin.minecraft.command.argument;

import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class ItemStringReaderExtender {
    @Mixin(targets = "net/minecraft/command/argument/ItemStringReader$Reader")
    public static class ReaderExtender {
        @ModifyArg(
            method = "method_57806",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/brigadier/exceptions/Dynamic2CommandExceptionType;createWithContext(Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;",
                remap = false
            ),
            index = 2
        )
        private Object indentErrorMessage(Object a) {
            return "\n" + StringUtil.indent(a.toString());
        }
    }
}
