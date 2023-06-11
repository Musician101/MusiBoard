package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.CommandSender;

public class ComponentArgumentType extends MusiBoardArgumentType<Component> {

    public static Component get(@Nonnull CommandContext<CommandSender> context, @Nonnull String name) {
        return context.getArgument(name, Component.class);
    }

    @Override
    public Component parse(StringReader reader) {
        String json = reader.getRemaining();
        reader.setCursor(reader.getCursor() + json.length());
        return GsonComponentSerializer.gson().deserialize(json);
    }
}
