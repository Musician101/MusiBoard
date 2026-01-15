package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandException;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.util.CheckedFunction;

import java.util.Arrays;
import java.util.List;

@NullMarked
public abstract class TeamArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Team> {

    public static TeamArgument withExecutor(CheckedFunction<CommandContext<CommandSourceStack>, Integer, CommandException> executor) {
        return new TeamArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                return executor.apply(context);
            }
        };
    }

    @SafeVarargs
    public static TeamArgument withChildren(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>... children) {
        return new TeamArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return Arrays.asList(children);
            }
        };
    }

    public static TeamArgument withChild(PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> child) {
        return new TeamArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(child);
            }
        };
    }

    @Override
    public String name() {
        return "team";
    }

    @Override
    public ArgumentType<Team> type() {
        return new TeamArgumentType();
    }
}
