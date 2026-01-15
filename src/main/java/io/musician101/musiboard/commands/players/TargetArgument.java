package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.Function;

@NullMarked
public abstract class TargetArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<String> {

    public static TargetArgument withChild(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> child) {
        return new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(child);
            }
        };
    }

    public static TargetArgument withExecutor(Function<CommandContext<CommandSourceStack>, Integer> executor) {
        return new TargetArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                return executor.apply(context);
            }
        };
    }

    public static TargetArgument withChildAndExecutor(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> child, Function<CommandContext<CommandSourceStack>, Integer> executor) {
        return new TargetArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                return executor.apply(context);
            }

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(child);
            }
        };
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
