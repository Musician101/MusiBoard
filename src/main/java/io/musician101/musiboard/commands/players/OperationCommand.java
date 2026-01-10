package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musiboard.commands.arguments.Operation;
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

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class OperationCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new TargetArgument() {

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
                                return new EnumArgumentType<>(Operation::operator, Operation.values());
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
