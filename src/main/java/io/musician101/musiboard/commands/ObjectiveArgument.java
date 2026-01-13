package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ObjectiveArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Objective> {

    @Override
    public String name() {
        return "objective";
    }

    @Override
    public ArgumentType<Objective> type() {
        return new ObjectiveArgumentType();
    }
}
