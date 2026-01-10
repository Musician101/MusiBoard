package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

@NullMarked
public class EnumArgumentType<E extends Enum<E>> implements ArgumentType<E> {

    private final Function<E, String> toString;
    private final E[] values;

    @SafeVarargs
    public EnumArgumentType(Function<E, String> toString, E... values) {
        this.toString = toString;
        this.values = values;
    }

    public static <E> E get(CommandContext<CommandSourceStack> context, String name, Class<E> clazz) {
        return context.getArgument(name, clazz);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Stream.of(values).map(toString).filter(e -> e.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public E parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        return Stream.of(values).filter(e -> string.equals(toString.apply(e))).findFirst().orElseThrow(() -> new SimpleCommandExceptionType(() -> "Invalid value for argument.").createWithContext(reader));
    }
}
