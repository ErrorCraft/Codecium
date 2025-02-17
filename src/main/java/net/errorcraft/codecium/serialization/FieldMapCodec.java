package net.errorcraft.codecium.serialization;

import com.mojang.serialization.*;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FieldMapCodec<A> extends MapCodec<A> {
    private final String name;
    private final MapEncoder<A> encoder;
    private final MapDecoder<A> decoder;
    private final Supplier<String> toStringSupplier;

    public FieldMapCodec(String name, MapEncoder<A> encoder, MapDecoder<A> decoder, Supplier<String> toStringSupplier) {
        this.name = name;
        this.encoder = encoder;
        this.decoder = decoder;
        this.toStringSupplier = toStringSupplier;
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return Stream.concat(this.encoder.keys(ops), this.decoder.keys(ops));
    }

    @Override
    public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return this.decoder.decode(ops, input)
            .mapError(message -> "'" + this.name + "': " + message);
    }

    @Override
    public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return this.encoder.encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return this.toStringSupplier.get();
    }
}
