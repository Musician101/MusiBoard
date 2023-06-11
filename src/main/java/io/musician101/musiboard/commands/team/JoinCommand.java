package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class JoinCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TeamArgument() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new TargetArgument() {

                    @Override
                    public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                        Team team = TeamArgumentType.get(context);
                        List<Entity> targets = getTargets(context, name());
                        team.addEntities(targets);
                        sendMessage(context, text("Added " + targets.size() + " member(s) to ", GREEN), team.displayName());
                        return 1;
                    }

                    @Nonnull
                    @Override
                    public String name() {
                        return "members";
                    }
                });
            }

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                Player player = getPlayer(context);
                team.addEntity(player);
                sendMessage(player, text("You have joined ", GREEN), team.displayName());
                return 1;
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Join a team or add members to a team.";
    }

    @Nonnull
    @Override
    public String name() {
        return "join";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/team join <team> [<members>]";
    }
}
