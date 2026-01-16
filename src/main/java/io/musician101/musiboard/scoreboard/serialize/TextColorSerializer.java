package io.musician101.musiboard.scoreboard.serialize;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@NullMarked
public class TextColorSerializer implements TypeSerializer<TextColor> {

    @Override
    public TextColor deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String colorString = node.require(String.class);
        TextColor color = TextColor.fromHexString(colorString);
        if (color == null) {
            return new NamedTextColorSerializer().deserialize(type, node);
        }

        return color;
    }

    @Override
    public void serialize(Type type, @Nullable TextColor obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        if (obj instanceof NamedTextColor) {
            new NamedTextColorSerializer().serialize(type, (NamedTextColor) obj, node);
            return;
        }

        node.set(obj.asHexString());
    }
}
