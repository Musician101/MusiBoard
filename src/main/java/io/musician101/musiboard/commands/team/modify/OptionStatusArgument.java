package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.CheckedBiFunction;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

@NullMarked
public interface OptionStatusArgument {

    static List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> get(CheckedBiFunction<CommandContext<CommandSourceStack>, OptionStatus, Integer, CommandException> executor) {
        Map<OptionStatus, String> nameMap = Map.of(OptionStatus.ALWAYS, "always", OptionStatus.NEVER, "never", OptionStatus.FOR_OTHER_TEAMS, "pushOtherTeams", OptionStatus.FOR_OWN_TEAM, "pushOwnTeam");
        return MBCommand.enumCommands(OptionStatus.values(), nameMap, executor);
    }
}
