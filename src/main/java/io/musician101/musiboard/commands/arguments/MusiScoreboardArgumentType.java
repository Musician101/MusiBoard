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
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

import static io.musician101.musiboard.MusiBoard.getManager;
import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class MusiScoreboardArgumentType implements CustomArgumentType<MusiScoreboard, String> {

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        getManager().getScoreboards().stream().map(MusiScoreboard::getName).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public MusiScoreboard parse(StringReader reader) throws CommandSyntaxException {
        return getScoreboard(reader.readString()).orElseThrow(() -> new SimpleCommandExceptionType(() -> "Scoreboard with that name does not exist.").createWithContext(reader));
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
