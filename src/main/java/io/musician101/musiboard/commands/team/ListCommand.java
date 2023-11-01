package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ListCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TeamArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                Player player = getPlayer(context);
                Set<String> entries = team.getEntries();
                int size = entries.size();
                Component entriesComponent = join(JoinConfiguration.separator(text(", ", GRAY)), entries.stream().map(e -> text(e, GREEN)).toList());
                sendMessage(player, team.displayName(), text(" has " + size + " member(s)" + (size > 0 ? ": " : ""), GREEN), entriesComponent);
                return 1;
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "List all of the teams or the players in a specified team.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = getPlayer(context);
        MusiScoreboard scoreboard = getScoreboard(player);
        Set<Team> teams = scoreboard.getTeams();
        int size = teams.size();
        Component teamsComponent = join(JoinConfiguration.separator(text(", ", GRAY)), teams.stream().map(Team::displayName).toList());
        sendMessage(player, text("There are " + size + " team(s)" + (size > 0 ? ": " : ""), GREEN), teamsComponent);
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "list";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/team list [<team>]";
    }
}
