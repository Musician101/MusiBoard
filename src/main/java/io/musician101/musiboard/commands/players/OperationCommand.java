package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musiboard.commands.arguments.Operation;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class OperationCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new TargetArgument() {

                    @Nonnull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new ArgumentCommand<Operation>() {

                            @Nonnull
                            @Override
                            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                                return List.of(new TargetArgument() {

                                    @Nonnull
                                    @Override
                                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                                        return List.of(new ObjectiveArgument() {

                                            @Override
                                            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                                                Objective targetObjective = getObjective(context, "targetObjective");
                                                Objective sourceObjective = getObjective(context, "sourceObjective");
                                                List<Entity> targets = getTargets(context, "targets");
                                                List<Entity> sources = getTargets(context, "sources");
                                                Operation operation = EnumArgumentType.get(context, "operation", Operation.class);
                                                for (Entity target : targets) {
                                                    Score a = targetObjective.getScoreFor(target);
                                                    for (Entity source : sources) {
                                                        Score b = sourceObjective.getScoreFor(source);
                                                        operation.apply(a, b);
                                                    }
                                                }

                                                int size = targets.size();
                                                sendMessage(context, text("Updated ", GREEN), targetObjective.displayName(), text(" for " + size + " entit" + (size == 1 ? "y" : "ies") + ".", GREEN));
                                                return 1;
                                            }

                                            @Nonnull
                                            @Override
                                            public String name() {
                                                return "sourceObjective";
                                            }
                                        });
                                    }

                                    @Nonnull
                                    @Override
                                    public String name() {
                                        return "sources";
                                    }
                                });
                            }

                            @Nonnull
                            @Override
                            public String name() {
                                return "operation";
                            }

                            @Nonnull
                            @Override
                            public ArgumentType<Operation> type() {
                                return new EnumArgumentType<>(Operation::operator, Operation.values());
                            }
                        });
                    }

                    @Nonnull
                    @Override
                    public String name() {
                        return "targetObjective";
                    }
                });
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Applies an arithmetic operation altering the targets' scores in the objective.";
    }

    @Nonnull
    @Override
    public String name() {
        return "operation";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/scoreboard players operation <targets> <targetObjective> <operation> <source> <sourceObjective>";
    }
}
