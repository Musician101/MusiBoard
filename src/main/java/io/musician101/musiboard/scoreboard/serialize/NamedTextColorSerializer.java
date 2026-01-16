package io.musician101.musiboard.scoreboard.serialize;

import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@NullMarked
public class NamedTextColorSerializer implements TypeSerializer<NamedTextColor> {

    @Override
    public NamedTextColor deserialize(Type type, ConfigurationNode node) throws SerializationException {
        NamedTextColor color =  NamedTextColor.NAMES.value(node.require(String.class));
        if (color == null) {
            return NamedTextColor.WHITE;
        }

        return color;
    }

    @Override
    public void serialize(Type type, @Nullable NamedTextColor obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.set(NamedTextColor.NAMES.key(obj));
    }
}
