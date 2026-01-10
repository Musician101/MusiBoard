package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.commands.arguments.TeamArgumentType.TeamValue;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class TeamArgumentType extends MusiBoardArgumentType<TeamValue> {

    public static Team get(CommandContext<CommandSourceStack> context) throws CommandException {
        return context.getArgument("team", TeamValue.class).getTeam(context);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof Player player) {
            MusiScoreboard scoreboard = getScoreboard(player);
            scoreboard.getTeams().stream().map(Team::getName).filter(name -> name.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public TeamValue parse(StringReader reader) throws CommandSyntaxException {
        return new TeamValue(reader.readString());
    }

    public class TeamValue {

        private final String teamName;

        TeamValue(String teamName) {
            this.teamName = teamName;
        }

        private Team getTeam(CommandContext<CommandSourceStack> context) throws CommandException {
            if (context.getSource() instanceof Player player) {
                Team team = getScoreboard(player).getTeam(teamName);
                if (team == null) {
                    throw new CommandException("A team with that name does not exist.");
                }

                return team;
            }

            throw new CommandException("Player only command.");
        }
    }
}
