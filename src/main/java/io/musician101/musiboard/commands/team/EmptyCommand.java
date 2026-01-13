package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class EmptyCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TeamArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Team team = TeamArgumentType.get(context);
                team.removeEntries(team.getEntries());
                sendMessage(context, "<green><mb-prefix> Removed all members from <team>", Messages.teamResolver(team));
                return 1;
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Empties a team of all members.");
    }

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team empty <team>");
    }
}
