package io.musician101.musiboard.commands.players;

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

public class ResetCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                        Player player = getPlayer(context);
                        Objective objective = getObjective(context);
                        getTargets(context).forEach(entity -> objective.getScoreFor(entity).resetScore());
                        sendMessage(player, text("Scores reset.", GREEN));
                        return 1;
                    }
                });
            }

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                getTargets(context).forEach(entity -> scoreboard.resetScores(entity.getName()));
                sendMessage(player, text("Scores reset.", GREEN));
                return 1;
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Deletes score or all scores for the targets.";
    }

    @NotNull
    @Override
    public String name() {
        return "reset";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/players reset <targets> [<objective>]";
    }
}
