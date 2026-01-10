package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.arguments.ComponentArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class DisplayNameArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Component> {

    protected Component get(CommandContext<CommandSourceStack> context) {
        return context.getArgument(name(), Component.class);
    }

    @Override
    public String name() {
        return "displayName";
    }

    @Override
    public ArgumentType<Component> type() {
        return new ComponentArgumentType();
    }
}
