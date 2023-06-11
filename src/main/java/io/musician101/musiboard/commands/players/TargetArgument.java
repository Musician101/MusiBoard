package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public abstract class TargetArgument extends MusiBoardCommand implements ArgumentCommand<String> {

    public Optional<Entity> getTarget(@Nonnull CommandContext<CommandSender> context) {
        return getTarget(context, name());
    }

    public Optional<Entity> getTarget(@Nonnull CommandContext<CommandSender> context, @Nonnull String name) {
        return Optional.of(getTargets(context, name)).filter(l -> !l.isEmpty()).map(l -> l.iterator().next());
    }

    public List<Entity> getTargets(@Nonnull CommandContext<CommandSender> context) {
        return getTargets(context, name());
    }

    public List<Entity> getTargets(@Nonnull CommandContext<CommandSender> context, @Nonnull String name) {
        return Bukkit.selectEntities(context.getSource(), context.getArgument(name, String.class));
    }

    @Nonnull
    @Override
    public String name() {
        return "targets";
    }

    @Nonnull
    @Override
    public ArgumentType<String> type() {
        return new EntitiesArgumentType();
    }
}
