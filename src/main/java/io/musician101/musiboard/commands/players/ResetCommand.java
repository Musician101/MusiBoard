package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class ResetCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    private void resetScores(CommandContext<CommandSourceStack> context, @Nullable Objective objective) {
        EntitiesArgumentType.getTargets(context, "targets").forEach(entity -> {
            String entityName = entity.getName();
            if (objective == null) {
                getScoreboard(getPlayer(context)).resetScores(entityName);
                return;
            }

            objective.getScore(entity.getName()).resetScore();
        });
        sendMessage(context, "<green><mb-prefix>Scores reset.");
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(TargetArgument.withChildAndExecutor(ObjectiveArgument.withExecutor(context -> {
            resetScores(context, ObjectiveArgumentType.get(context, "objective"));
            return 1;
        }), context -> {
            resetScores(context, null);
            return 1;
        }));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Deletes score or all scores for the targets.");
    }

    @Override
    public String name() {
        return "reset";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players reset <targets> [<objective>]");
    }
}
