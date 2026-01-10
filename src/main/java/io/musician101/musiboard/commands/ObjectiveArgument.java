package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType.ObjectiveValue;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ObjectiveArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<ObjectiveValue> {

    public Objective getObjective(CommandContext<CommandSourceStack> context) throws CommandException {
        return getObjective(context, name());
    }

    public Objective getObjective(CommandContext<CommandSourceStack> context, String name) throws CommandException {
        return context.getArgument(name, ObjectiveValue.class).get(context);
    }

    @Override
    public String name() {
        return "objective";
    }

    @Override
    public ArgumentType<ObjectiveValue> type() {
        return new ObjectiveArgumentType();
    }
}
