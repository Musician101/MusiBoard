package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType.ObjectiveValue;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

public abstract class ObjectiveArgument extends MusiBoardCommand implements ArgumentCommand<ObjectiveValue> {

    public Objective getObjective(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        return getObjective(context, name());
    }

    public Objective getObjective(@NotNull CommandContext<CommandSender> context, @NotNull String name) throws CommandSyntaxException {
        MusiScoreboard scoreboard = getScoreboard((Player) context.getSource());
        Objective objective = scoreboard.getObjective(context.getArgument(name, String.class));
        if (objective == null) {
            throw new SimpleCommandExceptionType(() -> "An objective with that name does not exist.").create();
        }

        return objective;
    }

    @NotNull
    @Override
    public String name() {
        return "objective";
    }

    @NotNull
    @Override
    public ArgumentType<ObjectiveValue> type() {
        return new ObjectiveArgumentType();
    }
}
