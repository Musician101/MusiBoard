package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jetbrains.annotations.NotNull;

public abstract class OptionStatusArgument extends MusiBoardCommand implements ArgumentCommand<OptionStatus> {

    @NotNull
    @Override
    public String name() {
        return "status";
    }

    @NotNull
    @Override
    public ArgumentType<OptionStatus> type() {
        return new EnumArgumentType<>(o -> o.toString().toLowerCase(), OptionStatus.values());
    }
}
