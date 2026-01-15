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
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class SetCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(TargetArgument.withChild(ObjectiveArgument.withChild(new ScoreArgument())));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Set the targets' score for an objective.");
    }

    @Override
    public String name() {
        return "set";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return PaperLiteralCommand.AdventureFormat.super.usage(source);
    }

    private class ScoreArgument implements PaperArgumentCommand.AdventureFormat<Integer> {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Objective objective = ObjectiveArgumentType.get(context, name());
            int s = IntegerArgumentType.getInteger(context, name());
            List<Entity> entities = EntitiesArgumentType.getTargets(context, "targets");
            entities.forEach(entity -> objective.getScoreFor(entity).setScore(s));
            int size = entities.size();
            String message = "<green><mb-prefix> Set <objective><green> for " + size + " entit" + (size == 1 ? "y" : "ies") + " to " + s + ".";
            sendMessage(context, message, Messages.objectiveResolver(objective));
            return 1;
        }

        @Override
        public String name() {
            return "set";
        }

        @Override
        public ArgumentType<Integer> type() {
            return IntegerArgumentType.integer();
        }
    }
}
