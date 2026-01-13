package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class TeamArgumentType implements CustomArgumentType<Team, String> {

    public static Team get(CommandContext<CommandSourceStack> context) throws CommandException {
        return context.getArgument("team", Team.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack source && source instanceof Player player) {
            MusiScoreboard scoreboard = getScoreboard(player);
            scoreboard.getTeams().stream().map(Team::getName).filter(name -> name.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public <S> Team parse(StringReader reader, S source) throws CommandSyntaxException {
        String name = reader.readString();
        if (source instanceof CommandSourceStack sourceStack && sourceStack.getSender() instanceof Player player) {
            Team team = getScoreboard(player).getTeam(name);
            if (team != null) {
                return team;
            }

            throw new SimpleCommandExceptionType(() -> "A team with that name does not exist.").createWithContext(reader);
        }

        throw new SimpleCommandExceptionType(() -> "This argument is only usable for player commands.").create();
    }

    @Override
    public Team parse(StringReader reader) throws CommandSyntaxException {
        throw new SimpleCommandExceptionType(() -> "This argument is dependant on the command source.").create();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
