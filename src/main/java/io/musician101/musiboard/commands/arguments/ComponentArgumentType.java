package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ComponentArgumentType extends MusiBoardArgumentType<Component> {

    public static Component get(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, Component.class);
    }

    @Override
    public Component parse(StringReader reader) {
        String json = reader.getRemaining();
        reader.setCursor(reader.getCursor() + json.length());
        return GsonComponentSerializer.gson().deserialize(json);
    }
}
