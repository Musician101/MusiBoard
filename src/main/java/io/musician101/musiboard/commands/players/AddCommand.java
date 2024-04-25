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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class AddCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @NotNull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new ArgumentCommand<Integer>() {

                            @Override
                            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                                Player player = getPlayer(context);
                                Objective objective = getObjective(context);
                                List<Entity> entities = getTargets(context);
                                int score = IntegerArgumentType.getInteger(context, name());
                                if (entities.isEmpty()) {
                                    sendMessage(player, text("No targets found.", RED));
                                    return 1;
                                }

                                entities.forEach(entity -> {
                                    Score s = objective.getScoreFor(entity);
                                    s.setScore(s.getScore() + score);
                                });

                                sendMessage(player, text("Added " + score + " to ", GREEN), objective.displayName(), text(" for " + entities.size() + " entit" + (entities.size() == 1 ? "y" : "ies") + "."));
                                return 1;
                            }

                            @NotNull
                            @Override
                            public String name() {
                                return "score";
                            }

                            @NotNull
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

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Increases the targets' scores in an objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "add";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/players add <targets> <objective> <score>";
    }
}
