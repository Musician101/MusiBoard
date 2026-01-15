package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class EntitiesArgumentType implements CustomArgumentType<String, String> {

    public static Optional<Entity> getTarget(CommandContext<CommandSourceStack> context, String name) {
        return Optional.of(getTargets(context, name)).filter(l -> !l.isEmpty()).map(List::getFirst);
    }

    public static List<Entity> getTargets(CommandContext<CommandSourceStack> context, String name) {
        return Bukkit.selectEntities(context.getSource().getSender(), context.getArgument(name, String.class));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(p -> p.startsWith(builder.getRemaining())).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
