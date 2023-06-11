package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.commands.arguments.TeamArgumentType.TeamValue;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TeamArgumentType extends MusiBoardArgumentType<TeamValue> {

    public static Team get(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
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

        @Nonnull
        private final String teamName;

        TeamValue(@Nonnull String teamName) {
            this.teamName = teamName;
        }

        @Nonnull
        private Team getTeam(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
            if (context.getSource() instanceof Player player) {
                Team team = getScoreboard(player).getTeam(teamName);
                if (team == null) {
                    throw new SimpleCommandExceptionType(() -> "A team with that name does not exist.").create();
                }

                return team;
            }

            throw new SimpleCommandExceptionType(() -> "Player only command.").create();
        }
    }
}
