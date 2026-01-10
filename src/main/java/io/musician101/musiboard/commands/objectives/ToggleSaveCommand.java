package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class ToggleSaveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new ObjectiveArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                Objective objective = getObjective(context);
                if (scoreboard.isObjectiveSaveDisabled(objective)) {
                    scoreboard.enableObjectiveSave(objective);
                    sendMessage(player, text("Objective save enabled.", GREEN));
                }
                else {
                    scoreboard.disableObjectiveSave(objective);
                    sendMessage(player, text("Objective save disabled.", GREEN));
                }

                return 1;
            }
        });
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
