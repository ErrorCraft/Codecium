package net.errorcraft.codecium.access.mojang.serialization.codecs;

public interface ListCodecAccess {
    default Object codecium$getAndRemoveTemporaryInput() {
        throw new AssertionError();
    }
}
