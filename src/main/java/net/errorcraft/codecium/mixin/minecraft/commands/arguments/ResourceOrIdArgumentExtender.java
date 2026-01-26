package net.errorcraft.codecium.mixin.minecraft.commands.arguments;

import net.errorcraft.codecium.util.StringUtil;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class ResourceOrIdArgumentExtender {
    @Mixin(ResourceOrIdArgument.InlineResult.class)
    public static class InlineResultExtender {
        @ModifyArg(
            method = "lambda$parse$0",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;createWithContext(Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"
            )
        )
        private static Object indentErrorMessage(Object arg) {
            return "\n" + StringUtil.indent(arg.toString());
        }
    }
}
