package net.errorcraft.codecium.mixin.minecraft.commands.arguments.item;

import net.errorcraft.codecium.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class ItemPredicateArgumentExtender {
    @Mixin(targets = "net/minecraft/commands/arguments/item/ItemPredicateArgument$ComponentWrapper")
    public static class ComponentWrapperExtender {
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

    @Mixin(targets = "net/minecraft/commands/arguments/item/ItemPredicateArgument$PredicateWrapper")
    public static class PredicateWrapperExtender {
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
