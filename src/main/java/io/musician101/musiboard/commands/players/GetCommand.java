package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class GetCommand extends MusiBoardCommand implements LiteralCommand {

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
                        Optional<Entity> optional = getTarget(context);
                        Objective objective = getObjective(context);
                        Player player = getPlayer(context);
                        if (optional.isEmpty()) {
                            sendMessage(player, text("No target found.", RED));
                            return 1;
                        }

                        Entity entity = optional.get();
                        int score = objective.getScoreFor(entity).getScore();
                        sendMessage(player, entity.name(), text(" has " + score + " ", GREEN), objective.displayName());
                        return 1;
                    }
                });
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Get the score of a target from an objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "get";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/players get <target> <objective>";
    }
}
