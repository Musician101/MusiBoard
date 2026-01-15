package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@NullMarked
public abstract class ObjectiveArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Objective> {

    public static ObjectiveArgument withChild(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> child) {
        return new ObjectiveArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(child);
            }
        };
    }

    public static ObjectiveArgument withChildren(List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children) {
        return new ObjectiveArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return children;
            }
        };
    }

    @SafeVarargs
    public static ObjectiveArgument withChildren(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>... children) {
        return withChildren(Arrays.asList(children));
    }

    public static ObjectiveArgument withExecutor(Function<CommandContext<CommandSourceStack>, Integer> executor) {
        return new ObjectiveArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                return executor.apply(context);
            }
        };
    }

    @Override
    public String name() {
        return "objective";
    }

    @Override
    public ArgumentType<Objective> type() {
        return new ObjectiveArgumentType();
    }
}
