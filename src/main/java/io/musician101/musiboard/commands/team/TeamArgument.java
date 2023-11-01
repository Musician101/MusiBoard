package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.commands.arguments.TeamArgumentType.TeamValue;
import org.jetbrains.annotations.NotNull;

public abstract class TeamArgument extends MusiBoardCommand implements ArgumentCommand<TeamValue> {

    @NotNull
    @Override
    public String name() {
        return "team";
    }

    @NotNull
    @Override
    public ArgumentType<TeamValue> type() {
        return new TeamArgumentType();
    }
}
