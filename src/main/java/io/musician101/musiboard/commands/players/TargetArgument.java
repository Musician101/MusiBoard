package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public abstract class TargetArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<String> {

    public Optional<Entity> getTarget(CommandContext<CommandSourceStack> context) {
        return getTarget(context, name());
    }

    public Optional<Entity> getTarget(CommandContext<CommandSourceStack> context, String name) {
        return Optional.of(getTargets(context, name)).filter(l -> !l.isEmpty()).map(List::getFirst);
    }

    public List<Entity> getTargets(CommandContext<CommandSourceStack> context) {
        return getTargets(context, name());
    }

    public List<Entity> getTargets(CommandContext<CommandSourceStack> context, String name) {
        return Bukkit.selectEntities(context.getSource().getSender(), context.getArgument(name, String.class));
    }

    @Override
    public String name() {
        return "targets";
    }

    @Override
    public ArgumentType<String> type() {
        return new EntitiesArgumentType();
    }
}
