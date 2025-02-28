package net.errorcraft.codecium.error;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Supplier;

public class RegistryErrorProvider {
    private RegistryErrorProvider() {}

    public static Supplier<String> inaccessibleRegistry(ResourceKey<? extends Registry<?>> registryKey, Holder<?> holder) {
        return () -> "Registry " + registryKey.location() + " is inaccessible for " + holder.unwrapKey().orElseThrow();
    }

    public static Supplier<String> noDirectHolder() {
        return () -> "Cannot encode a direct holder";
    }

    public static Supplier<String> unknownHolder(ResourceLocation id) {
        return () -> "Cannot get a holder with id " + id;
    }

    public static Supplier<String> unknownTag(TagKey<?> tag) {
        return () -> "Cannot get a registry tag with id " + tag.location();
    }
}
