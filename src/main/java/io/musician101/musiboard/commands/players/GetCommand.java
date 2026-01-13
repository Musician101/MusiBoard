package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.core.command.CommandException;
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

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
public class GetCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                        Optional<Entity> optional = getTarget(context);
                        Objective objective = ObjectiveArgumentType.get(context, name());
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
