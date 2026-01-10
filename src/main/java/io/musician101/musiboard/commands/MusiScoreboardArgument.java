package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.arguments.MusiScoreboardArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class MusiScoreboardArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<MusiScoreboard> {

    public MusiScoreboard getScoreboard(CommandContext<CommandSourceStack> context) {
        return context.getArgument(name(), MusiScoreboard.class);
    }

    @Override
    public String name() {
        return "name";
    }

    @Override
    public ArgumentType<MusiScoreboard> type() {
        return new MusiScoreboardArgumentType();
    }
}
