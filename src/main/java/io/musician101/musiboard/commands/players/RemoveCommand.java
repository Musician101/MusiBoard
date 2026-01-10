package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class RemoveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                        return List.of(new PaperArgumentCommand.AdventureFormat<Integer>() {

                            @Override
                            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                                List<Entity> entities = getTargets(context);
                                Player player = getPlayer(context);
                                Objective objective = getObjective(context);
                                int s = IntegerArgumentType.getInteger(context, name());
                                entities.forEach(entity -> {
                                    Score score = objective.getScoreFor(entity);
                                    score.setScore(score.getScore() - s);
                                });

                                int size = entities.size();
                                sendMessage(player, text("Removed " + s + " from ", GREEN), objective.displayName(), text(" for " + size + " entit" + (size == 1 ? "y" : "ies") + ".", GREEN));
                                return 1;
                            }

                            @Override
                            public String name() {
                                return "score";
                            }

                            @Override
                            public ArgumentType<Integer> type() {
                                return IntegerArgumentType.integer(0, Integer.MAX_VALUE);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Decreases the targets' scores in an objective.");
    }

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players remove <targets> <objective> <score>");
    }
}
