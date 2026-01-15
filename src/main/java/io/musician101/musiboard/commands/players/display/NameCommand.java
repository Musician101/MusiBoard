package io.musician101.musiboard.commands.players.display;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class NameCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(TargetArgument.withChild(ObjectiveArgument.withChild(new DisplayArgument())));
    }

    @Override
    public String name() {
        return "name";
    }

    private class DisplayArgument implements PaperArgumentCommand.AdventureFormat<Component> {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Objective objective = ObjectiveArgumentType.get(context, name());
            Component component = context.getArgument(name(), Component.class);
            EntitiesArgumentType.getTargets(context, "targets").forEach(e -> {
                Score score = objective.getScoreFor(e);
                score.customName(component);
            });
            sendMessage(context, "<green><mb-prefix>Custom player names updated for targets.");
            return 1;
        }

        @Override
        public String name() {
            return "display";
        }

        @Override
        public ArgumentType<Component> type() {
            return ArgumentTypes.component();
        }
    }
}
