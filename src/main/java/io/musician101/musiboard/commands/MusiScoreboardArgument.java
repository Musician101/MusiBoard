package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.arguments.MusiScoreboardArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class MusiScoreboardArgument extends MusiBoardCommand implements ArgumentCommand<MusiScoreboard> {

    @NotNull
    public MusiScoreboard getScoreboard(@NotNull CommandContext<CommandSender> context) {
        return context.getArgument(name(), MusiScoreboard.class);
    }

    @NotNull
    @Override
    public String name() {
        return "name";
    }

    @NotNull
    @Override
    public ArgumentType<MusiScoreboard> type() {
        return new MusiScoreboardArgumentType();
    }
}
