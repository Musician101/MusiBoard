package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.commands.arguments.OperationArgumentType;
import io.musician101.musiboard.commands.arguments.OperationArgumentType.Operation;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class OperationCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                        return List.of(new PaperArgumentCommand.AdventureFormat<Operation>() {

                            @Override
                            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                                return List.of(new TargetArgument() {

                                    @Override
                                    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                                        return List.of(new ObjectiveArgument() {

                                            @Override
                                            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                                                Objective targetObjective = ObjectiveArgumentType.get(context, "targetObjective");
                                                Objective sourceObjective = ObjectiveArgumentType.get(context, "sourceObjective");
                                                List<Entity> targets = getTargets(context, "targets");
                                                List<Entity> sources = getTargets(context, "sources");
                                                Operation operation = OperationArgumentType.get(context, "operation");
                                                for (Entity target : targets) {
                                                    Score a = targetObjective.getScoreFor(target);
                                                    for (Entity source : sources) {
                                                        Score b = sourceObjective.getScoreFor(source);
                                                        operation.apply(a, b);
                                                    }
                                                }

                                                int size = targets.size();
                                                sendMessage(context, "<green><mb-prefix>Updated <objective> <green>for " + size + " entit" + (size == 1 ? "y" : "ies") + ".", Messages.objectiveResolver(targetObjective));
                                                return 1;
                                            }

                                            @Override
                                            public String name() {
                                                return "sourceObjective";
                                            }
                                        });
                                    }

                                    @Override
                                    public String name() {
                                        return "sources";
                                    }
                                });
                            }

                            @Override
                            public String name() {
                                return "operation";
                            }

                            @Override
                            public ArgumentType<Operation> type() {
                                return new OperationArgumentType();
                            }
                        });
                    }

                    @Override
                    public String name() {
                        return "targetObjective";
                    }
                });
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Applies an arithmetic operation altering the targets' scores in the objective.");
    }

    @Override
    public String name() {
        return "operation";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/scoreboard players operation <targets> <targetObjective> <operation> <source> <sourceObjective>");
    }
}
