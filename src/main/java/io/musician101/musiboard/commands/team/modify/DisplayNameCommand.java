package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.DisplayNameArgument;
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
public class DisplayNameCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new DisplayNameArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Component displayName = get(context);
                Team team = TeamArgumentType.get(context);
                team.displayName(displayName);
                sendMessage(context, "<green><mb-prefix> Team display name updated to <team>", Messages.teamResolver(team));
                return 1;
            }
        });
    }

    @Override
    public String name() {
        return "displayName";
    }
}
