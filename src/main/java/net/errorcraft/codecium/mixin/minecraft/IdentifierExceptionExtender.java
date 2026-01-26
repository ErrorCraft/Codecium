package net.errorcraft.codecium.mixin.minecraft;

import net.errorcraft.codecium.access.minecraft.IdentifierExceptionAccess;
import net.minecraft.IdentifierException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IdentifierException.class)
public class IdentifierExceptionExtender extends RuntimeException implements IdentifierExceptionAccess {
    @Unique
    private String givenIdentifier;

    @Override
    public String getMessage() {
        if (this.givenIdentifier == null) {
            return super.getMessage();
        }

        return super.getMessage() + ": " + this.givenIdentifier;
    }

    @Override
    public String codecium$messageWithoutId() {
        return super.getMessage();
    }

    @Override
    public void codecium$setGivenIdentifier(String givenIdentifier) {
        this.givenIdentifier = givenIdentifier;
    }
}
