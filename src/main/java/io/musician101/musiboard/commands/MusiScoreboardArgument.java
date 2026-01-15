package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.arguments.MusiScoreboardArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public abstract class MusiScoreboardArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<MusiScoreboard> {

    public static MusiScoreboardArgument withExecutor(Function<CommandContext<CommandSourceStack>, Integer> executor) {
        return new MusiScoreboardArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                return executor.apply(context);
            }
        };
    }

    public MusiScoreboard getScoreboard(CommandContext<CommandSourceStack> context) {
        return context.getArgument(name(), MusiScoreboard.class);
    }

    @Override
    public String name() {
        return "scoreboard";
    }

    @Override
    public ArgumentType<MusiScoreboard> type() {
        return new MusiScoreboardArgumentType();
    }
}
