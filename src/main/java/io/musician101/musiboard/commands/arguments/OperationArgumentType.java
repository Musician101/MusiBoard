package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.musiboard.commands.arguments.OperationArgumentType.Operation;
import io.musician101.musicommand.core.command.CommandException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class OperationArgumentType implements CustomArgumentType<Operation, String> {

    public static Operation get(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, Operation.class);
    }

    @Override
    public Operation parse(StringReader reader) throws CommandSyntaxException {
        String operator = reader.readString();
        if (operator.equals("><")) {
            return (target, source) -> {
                int i = target.getScore();
                target.setScore(source.getScore());
                source.setScore(i);
            };
        }

        return simpleOperation(operator);
    }

    private SimpleOperation simpleOperation(String operator) throws CommandSyntaxException {
        return switch (operator) {
            case "=" -> (target, source) -> source;
            case "+=" -> Integer::sum;
            case "-=" -> (target, source) -> target - source;
            case "*=" -> (target, source) -> target * source;
            case "/=" -> (target, source) -> {
                if (source == 0) {
                    throw new CommandException("Can not divide by zero.");
                }

                return target / source;
            };
            case "%=" -> (target, source) -> {
                if (source == 0) {
                    throw new CommandException("Can not divide by zero.");
                }

                return Math.floorMod(target, source);
            };
            case "<" -> Math::min;
            case ">" -> Math::max;
            default -> throw new SimpleCommandExceptionType(() -> "Invalid operation.").create();
        };
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> suggestions = List.of("=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><");
        suggestions.stream().filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @FunctionalInterface
    public interface Operation {

        void apply(Score target, Score source) throws CommandException;
    }

    @FunctionalInterface
    public interface SimpleOperation extends Operation {

        int apply(int target, int source) throws CommandException;

        @Override
        default void apply(Score target, Score source) throws CommandException {
            target.setScore(apply(target.getScore(), source.getScore()));
        }
    }
}
