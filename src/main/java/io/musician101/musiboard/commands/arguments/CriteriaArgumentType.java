package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@NullMarked
public class CriteriaArgumentType extends MusiBoardArgumentType<Criteria> {

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Stream.of(Criteria.class.getFields()).map(field -> {
            try {
                return field.get(null);
            }
            catch (IllegalAccessException e) {
                return null;
            }
        }).filter(Objects::nonNull).filter(Criteria.class::isInstance).map(Criteria.class::cast).map(Criteria::getName).filter(name -> name.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Criteria parse(StringReader reader) throws CommandSyntaxException {
        return Bukkit.getScoreboardCriteria(reader.readString());
    }
}
