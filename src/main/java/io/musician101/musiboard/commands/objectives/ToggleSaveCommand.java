package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ToggleSaveCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ObjectiveArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
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

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Toggle whether an objective will be saved to a file.";
    }

    @NotNull
    @Override
    public String name() {
        return "toggleSave";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/objectives toggleSave <objective>";
    }
}
