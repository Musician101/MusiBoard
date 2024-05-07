package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class TargetArgument extends MusiBoardCommand implements ArgumentCommand<String> {

    public Optional<Entity> getTarget(@NotNull CommandContext<CommandSender> context) {
        return getTarget(context, name());
    }

    public Optional<Entity> getTarget(@NotNull CommandContext<CommandSender> context, @NotNull String name) {
        return Optional.of(getTargets(context, name)).filter(l -> !l.isEmpty()).map(List::getFirst);
    }

    public List<Entity> getTargets(@NotNull CommandContext<CommandSender> context) {
        return getTargets(context, name());
    }

    public List<Entity> getTargets(@NotNull CommandContext<CommandSender> context, @NotNull String name) {
        return Bukkit.selectEntities(context.getSource(), context.getArgument(name, String.class));
    }

    @NotNull
    @Override
    public String name() {
        return "targets";
    }

    @NotNull
    @Override
    public ArgumentType<String> type() {
        return new EntitiesArgumentType();
    }
}
