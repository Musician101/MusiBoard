package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class LeaveCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                List<Entity> targets = getTargets(context, name());
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                scoreboard.getTeams().forEach(team -> targets.forEach(team::removeEntity));
                sendMessage(player, text("Removed " + targets.size() + " member(s) from team(s).", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "members";
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Makes the specified entities leave their teams.";
    }

    @NotNull
    @Override
    public String name() {
        return "leave";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/team leave <members>";
    }
}
