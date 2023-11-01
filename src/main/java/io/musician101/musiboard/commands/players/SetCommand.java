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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SetCommand extends MusiBoardCommand implements LiteralCommand {

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
                                Objective objective = getObjective(context);
                                int s = IntegerArgumentType.getInteger(context, name());
                                List<Entity> entities = getTargets(context);
                                entities.forEach(entity -> objective.getScoreFor(entity).setScore(s));
                                int size = entities.size();
                                sendMessage(context, text("Set ", GREEN), objective.displayName(), text(" for " + size + " entit" + (size == 1 ? "y" : "ies") + " to " + s + ".", GREEN));
                                return 1;
                            }

                            @NotNull
                            @Override
                            public String name() {
                                return "set";
                            }

                            @NotNull
                            @Override
                            public ArgumentType<Integer> type() {
                                return IntegerArgumentType.integer();
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
        return "Set the targets' score for an objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "set";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return LiteralCommand.super.usage(sender);
    }
}
