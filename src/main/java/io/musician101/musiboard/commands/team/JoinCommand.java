package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class JoinCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TeamArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new TargetArgument() {

                    @Override
                    public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                        Team team = TeamArgumentType.get(context);
                        List<Entity> targets = getTargets(context, name());
                        team.addEntities(targets);
                        sendMessage(context, "<green><mb-prefix> Added " + targets.size() + " member(s) to <team>", Messages.teamResolver(team));
                        return 1;
                    }

                    @Override
                    public String name() {
                        return "members";
                    }
                });
            }

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Team team = TeamArgumentType.get(context);
                Player player = getPlayer(context);
                team.addEntity(player);
                sendMessage(player, "<green><mb-prefix> You have joined <team>", Messages.teamResolver(team));
                return 1;
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Join a team or add members to a team.");
    }

    @Override
    public String name() {
        return "join";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team join <team> [<members>]");
    }
}
