package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musicommand.core.command.CommandException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class ObjectiveArgumentType implements CustomArgumentType<Objective, String> {

    public static Objective get(CommandContext<CommandSourceStack> context, String name) throws CommandException {
        return context.getArgument(name, Objective.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack source && source.getSender() instanceof Player player) {
            getScoreboard(player).getObjectives().stream().map(Objective::getName).filter(o -> o.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public <S> Objective parse(StringReader reader, S source) throws CommandSyntaxException {
        String name = reader.readString();
        if (source instanceof CommandSourceStack sourceStack && sourceStack.getSender() instanceof Player) {
            Objective objective = getScoreboard((Player) sourceStack.getSender()).getObjective(name);
            if (objective != null) {
                return objective;
            }

            throw new SimpleCommandExceptionType(() -> "An objective with that name does not exist.").createWithContext(reader);
        }

        throw new SimpleCommandExceptionType(() -> "This argument is only usable for player commands.").create();
    }

    @Override
    public Objective parse(StringReader reader) throws CommandSyntaxException {
        throw new SimpleCommandExceptionType(() -> "This argument is dependant on the command source.").create();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
