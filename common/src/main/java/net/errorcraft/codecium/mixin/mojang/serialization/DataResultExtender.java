package net.errorcraft.codecium.mixin.mojang.serialization;

import com.mojang.serialization.DataResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DataResult.class, remap = false)
public interface DataResultExtender {
    /**
     * @author ErrorCraft
     * @reason Uses a line feed for a better error message. Also swaps the order of the messages to preserve the order in the codecs.
     */
    @Overwrite
    static String appendMessages(final String first, final String second) {
        return second + "\n" + first;
    }
}
