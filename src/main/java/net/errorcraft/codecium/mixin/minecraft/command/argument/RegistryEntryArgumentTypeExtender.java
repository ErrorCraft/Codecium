package net.errorcraft.codecium.mixin.minecraft.command.argument;

import net.errorcraft.codecium.util.StringUtil;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RegistryEntryArgumentType.class)
public class RegistryEntryArgumentTypeExtender {
    @ModifyArg(
        method = "method_58480",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;createWithContext(Lcom/mojang/brigadier/ImmutableStringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;",
            remap = false
        )
    )
    private static Object indentErrorMessage(Object arg) {
        return "\n" + StringUtil.indent(arg.toString());
    }
}
