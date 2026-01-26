package net.errorcraft.codecium.mixin.minecraft;

import net.errorcraft.codecium.access.minecraft.util.InvalidIdentifierExceptionAccess;
import net.minecraft.ResourceLocationException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ResourceLocationException.class)
public class IdentifierExceptionExtender extends RuntimeException implements InvalidIdentifierExceptionAccess {
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
