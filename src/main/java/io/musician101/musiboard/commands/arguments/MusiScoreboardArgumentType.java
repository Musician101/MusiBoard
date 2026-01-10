package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class MusiScoreboardArgumentType extends MusiBoardArgumentType<MusiScoreboard> {

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getManager().getScoreboards().stream().map(MusiScoreboard::getName).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public MusiScoreboard parse(StringReader reader) throws CommandSyntaxException {
        return getManager().getScoreboard(reader.readString()).orElseThrow(() -> new SimpleCommandExceptionType(() -> "Scoreboard with that name does not exist.").createWithContext(reader));
    }
}
