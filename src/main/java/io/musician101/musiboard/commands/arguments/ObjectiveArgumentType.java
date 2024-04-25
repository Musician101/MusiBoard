package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType.ObjectiveValue;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ObjectiveArgumentType extends MusiBoardArgumentType<ObjectiveValue> {

    public static Objective get(@NotNull CommandContext<CommandSender> context, @NotNull String name) throws CommandSyntaxException {
        return context.getArgument(name, ObjectiveValue.class).get(context);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof Player player) {
            getScoreboard(player).getObjectives().stream().map(Objective::getName).filter(o -> o.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public ObjectiveValue parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        return context -> {
            Objective objective = getScoreboard((Player) context.getSource()).getObjective(name);
            if (objective == null) {
                throw new SimpleCommandExceptionType(() -> "An objective with that name does not exist.").create();
            }

            return objective;
        };
    }

    public interface ObjectiveValue {

        @NotNull
        Objective get(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException;
    }
}
