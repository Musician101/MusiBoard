package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.arguments.ComponentArgumentType;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public abstract class DisplayNameArgument extends MusiBoardCommand implements ArgumentCommand<Component> {

    protected Component get(CommandContext<CommandSender> context) {
        return context.getArgument(name(), Component.class);
    }

    @Nonnull
    @Override
    public String name() {
        return "displayName";
    }

    @Nonnull
    @Override
    public ArgumentType<Component> type() {
        return new ComponentArgumentType();
    }
}
