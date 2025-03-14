package net.errorcraft.codecium.mixin.minecraft.command.argument;

import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class ItemPredicateArgumentTypeExtender {
    @Mixin(targets = "net/minecraft/command/argument/ItemPredicateArgumentType$ComponentCheck")
    public static class ComponentCheckExtender {
        @ModifyArg(
            method = "method_58537",
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

    @Mixin(targets = "net/minecraft/command/argument/ItemPredicateArgumentType$SubPredicateCheck")
    public static class SubPredicateCheckExtender {
        @ModifyArg(
            method = "method_58560",
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
