package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.arguments.MusiScoreboardArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

public abstract class MusiScoreboardArgument extends MusiBoardCommand implements ArgumentCommand<MusiScoreboard> {

    @Nonnull
    public MusiScoreboard getScoreboard(@Nonnull CommandContext<CommandSender> context) {
        return context.getArgument(name(), MusiScoreboard.class);
    }

    @Nonnull
    @Override
    public String name() {
        return "name";
    }

    @Nonnull
    @Override
    public ArgumentType<MusiScoreboard> type() {
        return new MusiScoreboardArgumentType();
    }
}
