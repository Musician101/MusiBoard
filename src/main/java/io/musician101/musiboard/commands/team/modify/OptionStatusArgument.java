package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import javax.annotation.Nonnull;
import org.bukkit.scoreboard.Team.OptionStatus;

public abstract class OptionStatusArgument extends MusiBoardCommand implements ArgumentCommand<OptionStatus> {

    @Nonnull
    @Override
    public String name() {
        return "status";
    }

    @Nonnull
    @Override
    public ArgumentType<OptionStatus> type() {
        return new EnumArgumentType<>(o -> o.toString().toLowerCase(), OptionStatus.values());
    }
}
