package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class TextColorArgumentType implements ArgumentType<NamedTextColor> {

    public static NamedTextColor getColor(@Nonnull CommandContext<CommandSender> context) {
        return context.getArgument("color", NamedTextColor.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        NamedTextColor.NAMES.keys().stream().filter(s -> s.equals(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public NamedTextColor parse(StringReader reader) throws CommandSyntaxException {
        String colorString = reader.readString();
        NamedTextColor color = NamedTextColor.NAMES.value(colorString);
        if (color == null) {
            throw new SimpleCommandExceptionType(() -> "Color does not exist.").createWithContext(reader);
        }

        return color;
    }
}
