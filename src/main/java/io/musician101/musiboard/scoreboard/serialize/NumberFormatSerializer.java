package io.musician101.musiboard.scoreboard.serialize;

import io.papermc.paper.scoreboard.numbers.FixedFormat;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import io.papermc.paper.scoreboard.numbers.StyledFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map.Entry;

@NullMarked
public class NumberFormatSerializer implements TypeSerializer<NumberFormat> {

    @Override
    public NumberFormat deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String formatType = node.node("type").require(String.class);
        return switch (formatType) {
            case "minecraft:blank" -> NumberFormat.blank();
            case "minecraft:fixed" -> {
                Component value = GsonComponentSerializer.gson().deserialize(node.node("value").require(String.class));
                yield NumberFormat.fixed(value);
            }
            case "minecraft:result" -> {
                Style.Builder builder = Style.style();
                TextColor color = node.node("color").get(TextColor.class);
                if (color != null) {
                    builder.apply(color);
                }

                TextDecoration.NAMES.keyToValue().forEach((key, value) -> {
                    boolean isApplied = node.node(key).getBoolean();
                    if (isApplied) {
                        builder.apply(value);
                    }
                });
                yield NumberFormat.styled(builder.build());
            }
            default -> throw new SerializationException(formatType + " is not a valid format.");
        };
    }

    @Override
    public void serialize(Type type, @Nullable NumberFormat obj, ConfigurationNode node) throws SerializationException {
        switch (obj) {
            case null -> node.set(null);
            case FixedFormat fixed -> {
                node.node("type").set("minecraft:fixed");
                node.node("value").set(GsonComponentSerializer.gson().serialize(fixed.component()));
            }
            case StyledFormat styled -> {
                node.node("type").set("minecraft:styled");
                Style style = styled.style();
                node.node("color").set(style.color());
                for (Entry<TextDecoration, State> entry : style.decorations().entrySet()) {
                    TextDecoration deco = entry.getKey();
                    State state = entry.getValue();
                    if (state == State.TRUE) {
                        // Index#key() is Nullable, but in this case it *should* be fine.
                        node.node(TextDecoration.NAMES.key(deco)).set(true);
                    }
                }
            }
            default -> node.node("type").set("minecraft:blank");
        }
    }
}
