package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class RemoveCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @Nonnull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new ArgumentCommand<Integer>() {

                            @Override
                            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
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

                            @Nonnull
                            @Override
                            public String name() {
                                return "score";
                            }

                            @Nonnull
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

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Decreases the targets' scores in an objective.";
    }

    @Nonnull
    @Override
    public String name() {
        return "remove";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/players remove <targets> <objective> <score>";
    }
}
