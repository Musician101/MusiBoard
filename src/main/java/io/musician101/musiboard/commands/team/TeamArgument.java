package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.commands.arguments.TeamArgumentType.TeamValue;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class TeamArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<TeamValue> {

    @Override
    public String name() {
        return "team";
    }

    @Override
    public ArgumentType<TeamValue> type() {
        return new TeamArgumentType();
    }
}
