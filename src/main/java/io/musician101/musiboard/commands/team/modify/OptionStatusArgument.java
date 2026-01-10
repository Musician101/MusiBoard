package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class OptionStatusArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<OptionStatus> {

    @Override
    public String name() {
        return "status";
    }

    @Override
    public ArgumentType<OptionStatus> type() {
        return new EnumArgumentType<>(o -> o.toString().toLowerCase(), OptionStatus.values());
    }
}
