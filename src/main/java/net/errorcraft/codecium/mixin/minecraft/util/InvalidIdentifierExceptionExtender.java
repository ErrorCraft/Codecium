package net.errorcraft.codecium.mixin.minecraft.util;

import net.errorcraft.codecium.access.minecraft.util.InvalidIdentifierExceptionAccess;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(InvalidIdentifierException.class)
public class InvalidIdentifierExceptionExtender extends RuntimeException implements InvalidIdentifierExceptionAccess {
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
