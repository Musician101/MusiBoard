package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.commands.arguments.TeamArgumentType.TeamValue;
import javax.annotation.Nonnull;

public abstract class TeamArgument extends MusiBoardCommand implements ArgumentCommand<TeamValue> {

    @Nonnull
    @Override
    public String name() {
        return "team";
    }

    @Nonnull
    @Override
    public ArgumentType<TeamValue> type() {
        return new TeamArgumentType();
    }
}
