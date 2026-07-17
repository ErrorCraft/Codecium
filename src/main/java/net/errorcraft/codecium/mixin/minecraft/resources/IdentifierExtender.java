package net.errorcraft.codecium.mixin.minecraft.resources;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.codecium.access.minecraft.IdentifierExceptionAccess;
import net.minecraft.IdentifierException;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    private static boolean validNamespaceChar(char c) {
        return false;
    }

    @Shadow
    public static boolean validPathChar(char c) {
        return false;
    }

    @ModifyArg(
        method = "read(Ljava/lang/String;)Lcom/mojang/serialization/DataResult;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static Supplier<String> identifierExceptionUseBetterMessage(Supplier<String> message, @Local(argsOnly = true, name = "input") String input, @Local(name = "e") IdentifierException e) {
        return () -> ((IdentifierExceptionAccess) e).codecium$messageWithoutId() + ": " + input;
    }

    @Redirect(
        method = "assertValidNamespace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resources/Identifier;isValidNamespace(Ljava/lang/String;)Z"
        )
    )
    private static boolean validateNamespaceUseBetterMessage(String namespace, @Local(argsOnly = true, name = "path") String path) {
        for (int i = 0; i < namespace.length(); i++) {
            if (!validNamespaceChar(namespace.charAt(i))) {
                IdentifierException exception = new IdentifierException("Invalid character '" + namespace.charAt(i) + "' in namespace of resource location");
                ((IdentifierExceptionAccess) exception).codecium$setGivenIdentifier(namespace + NAMESPACE_SEPARATOR + path);
                throw exception;
            }
        }

        return true;
    }

    @Redirect(
        method = "assertValidPath",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resources/Identifier;isValidPath(Ljava/lang/String;)Z"
        )
    )
    private static boolean validatePathUseBetterMessage(String path, String namespace) {
        for (int i = 0; i < path.length(); i++) {
            if (!validPathChar(path.charAt(i))) {
                IdentifierException exception = new IdentifierException("Invalid character '" + path.charAt(i) + "' in path of resource location");
                ((IdentifierExceptionAccess) exception).codecium$setGivenIdentifier(namespace + NAMESPACE_SEPARATOR + path);
                throw exception;
            }
        }

        return true;
    }
}
