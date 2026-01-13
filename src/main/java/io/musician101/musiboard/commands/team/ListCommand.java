package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class ListCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TeamArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Team team = TeamArgumentType.get(context);
                Player player = getPlayer(context);
                Set<String> entries = team.getEntries();
                int size = entries.size();
                Component entriesComponent = join(JoinConfiguration.separator(text(", ", GRAY)), entries.stream().map(e -> text(e, GREEN)).toList());
                TagResolver resolver = TagResolver.resolver("entries", Tag.selfClosingInserting(entriesComponent));
                String message = "<green><mb-prefix> <team><green> has " + size + " member(s)" + (size > 0 ? ": <entries>" : "");
                sendMessage(player, message, resolver);
                return 1;
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("List all of the teams or the players in a specified team.");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayer(context);
        MusiScoreboard scoreboard = getScoreboard(player);
        Set<Team> teams = scoreboard.getTeams();
        int size = teams.size();
        Component teamsComponent = join(JoinConfiguration.separator(text(", ", GRAY)), teams.stream().map(Team::displayName).toList());
        TagResolver resolver = TagResolver.resolver("teams", Tag.selfClosingInserting(teamsComponent));
        sendMessage(player, "<green><mb-prefix> There are " + size + " team(s)" + (size > 0 ? ": <teams>" : ""), resolver);
        return 1;
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team list [<team>]");
    }
}
