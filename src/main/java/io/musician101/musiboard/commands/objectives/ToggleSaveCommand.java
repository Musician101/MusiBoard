package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class ToggleSaveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(ObjectiveArgument.withExecutor(context -> {
            Player player = getPlayer(context);
            MusiScoreboard scoreboard = getScoreboard(player);
            Objective objective = ObjectiveArgumentType.get(context, name());
            if (scoreboard.isObjectiveSaveDisabled(objective)) {
                scoreboard.enableObjectiveSave(objective);
                sendMessage(player, "<green><mb-prefix>Objective save enabled.");
            }
            else {
                scoreboard.disableObjectiveSave(objective);
                sendMessage(player, "<green><mb-prefix>Objective save disabled.");
            }

            return 1;
        }));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Toggle whether an objective will be saved to a file.");
    }

    @Override
    public String name() {
        return "toggleSave";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives toggleSave <objective>");
    }
}
