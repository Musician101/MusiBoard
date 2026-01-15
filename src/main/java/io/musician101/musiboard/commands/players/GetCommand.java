package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public class GetCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(TargetArgument.withChild(ObjectiveArgument.withExecutor(context -> {
            Optional<Entity> optional = EntitiesArgumentType.getTarget(context, "targets");
            Objective objective = ObjectiveArgumentType.get(context, name());
            Player player = getPlayer(context);
            if (optional.isEmpty()) {
                sendMessage(player, "<red><mb-prefix>No target found.");
                return 1;
            }

            Entity entity = optional.get();
            int score = objective.getScoreFor(entity).getScore();
            sendMessage(player, "<green><mb-prefix> " + entity.name() + " has " + score + " <objective>", Messages.objectiveResolver(objective));
            return 1;
        })));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Get the score of a target from an objective.");
    }

    @Override
    public String name() {
        return "get";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players get <target> <objective>");
    }
}
