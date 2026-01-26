package net.errorcraft.codecium.mixin.minecraft.commands.arguments.item;

import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class ItemParserExtender {
    @Mixin(targets = "net/minecraft/commands/arguments/item/ItemParser$State")
    public static class StateExtender {
        @ModifyArg(
            method = "lambda$readComponent$0",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/brigadier/exceptions/Dynamic2CommandExceptionType;createWithContext(Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"
            ),
            index = 2
        )
        private Object indentErrorMessage(Object a) {
            return "\n" + StringUtil.indent(a.toString());
        }
    }
}
