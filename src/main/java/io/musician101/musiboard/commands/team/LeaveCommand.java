package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class LeaveCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) {
                List<Entity> targets = getTargets(context, name());
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                scoreboard.getTeams().forEach(team -> targets.forEach(team::removeEntity));
                sendMessage(player, text("Removed " + targets.size() + " member(s) from team(s).", GREEN));
                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "members";
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Makes the specified entities leave their teams.";
    }

    @Nonnull
    @Override
    public String name() {
        return "leave";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/team leave <members>";
    }
}
