package net.errorcraft.codecium.mixin.minecraft.util;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.codecium.access.minecraft.util.InvalidIdentifierExceptionAccess;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(Identifier.class)
public abstract class IdentifierExtender {
    @Shadow
    @Final
    public static char NAMESPACE_SEPARATOR;

    @Shadow
    private static boolean isNamespaceCharacterValid(char character) {
        return false;
    }

    @Shadow
    public static boolean isPathCharacterValid(char character) {
        return false;
    }

    @ModifyArg(
        method = "validate",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static Supplier<String> identifierExceptionUseBetterMessage(Supplier<String> message, @Local(argsOnly = true) String id, @Local InvalidIdentifierException exception) {
        return () -> ((InvalidIdentifierExceptionAccess) exception).codecium$messageWithoutId() + ": " + id;
    }

    @Redirect(
        method = "validateNamespace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Identifier;isNamespaceValid(Ljava/lang/String;)Z"
        )
    )
    @Unique
    private static boolean validateNamespaceUseBetterMessage(String namespace, String path) {
        for (int i = 0; i < namespace.length(); ++i) {
            if (!isNamespaceCharacterValid(namespace.charAt(i))) {
                InvalidIdentifierException exception = new InvalidIdentifierException("Invalid character '" + namespace.charAt(i) + "' in namespace of resource location");
                ((InvalidIdentifierExceptionAccess) exception).codecium$setGivenIdentifier(namespace + NAMESPACE_SEPARATOR + path);
                throw exception;
            }
        }
        return true;
    }

    @Redirect(
        method = "validatePath",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Identifier;isPathValid(Ljava/lang/String;)Z"
        )
    )
    private static boolean validatePathUseBetterMessage(String path, String namespace) {
        for (int i = 0; i < path.length(); ++i) {
            if (!isPathCharacterValid(path.charAt(i))) {
                InvalidIdentifierException exception = new InvalidIdentifierException("Invalid character '" + path.charAt(i) + "' in path of resource location");
                ((InvalidIdentifierExceptionAccess) exception).codecium$setGivenIdentifier(namespace + NAMESPACE_SEPARATOR + path);
                throw exception;
            }
        }
        return true;
    }
}
