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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class OperationCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new TargetArgument() {

                    @NotNull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new ArgumentCommand<Operation>() {

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

                                            @NotNull
                                            @Override
                                            public String name() {
                                                return "sourceObjective";
                                            }
                                        });
                                    }

                                    @NotNull
                                    @Override
                                    public String name() {
                                        return "sources";
                                    }
                                });
                            }

                            @NotNull
                            @Override
                            public String name() {
                                return "operation";
                            }

                            @NotNull
                            @Override
                            public ArgumentType<Operation> type() {
                                return new EnumArgumentType<>(Operation::operator, Operation.values());
                            }
                        });
                    }

                    @NotNull
                    @Override
                    public String name() {
                        return "targetObjective";
                    }
                });
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Applies an arithmetic operation altering the targets' scores in the objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "operation";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/scoreboard players operation <targets> <targetObjective> <operation> <source> <sourceObjective>";
    }
}
