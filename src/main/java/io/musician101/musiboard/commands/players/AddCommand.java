package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class AddCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(TargetArgument.withChild(ObjectiveArgument.withChild(new ScoreArgument())));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Increases the targets' scores in an objective.");
    }

    @Override
    public String name() {
        return "add";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players add <targets> <objective> <score>");
    }

    private static class ScoreArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Integer> {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Player player = getPlayer(context);
            Objective objective = ObjectiveArgumentType.get(context, name());
            List<Entity> entities = EntitiesArgumentType.getTargets(context, "targets");
            int score = IntegerArgumentType.getInteger(context, name());
            if (entities.isEmpty()) {
                sendMessage(player, "<red><mb-prefix>No targets found.");
                return 1;
            }

            entities.forEach(entity -> {
                Score s = objective.getScoreFor(entity);
                s.setScore(s.getScore() + score);
            });

            sendMessage(player, "<green><mb-prefix>Added " + score + " to <objective>" + " for " + entities.size() + " entit" + (entities.size() == 1 ? "y" : "ies") + ".", Messages.objectiveResolver(objective));
            return 1;
        }

        @Override
        public String name() {
            return "score";
        }

        @Override
        public ArgumentType<Integer> type() {
            return IntegerArgumentType.integer(0, Integer.MAX_VALUE);
        }
    }
}
