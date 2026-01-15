package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
class RemoveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(ObjectiveArgument.withExecutor(context -> {
            Objective objective = ObjectiveArgumentType.get(context, "objective");
            objective.unregister();
            sendMessage(context, "<green><mb-prefix>Objective removed.");
            return 1;
        }));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Remove an objective.");
    }

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives remove <objective>");
    }
}
