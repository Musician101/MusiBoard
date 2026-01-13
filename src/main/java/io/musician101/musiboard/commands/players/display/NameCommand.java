package io.musician101.musiboard.commands.players.display;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class NameCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                        return List.of(new PaperArgumentCommand.AdventureFormat<Component>() {

                            @Override
                            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                                Objective objective = ObjectiveArgumentType.get(context, name());
                                Component component = context.getArgument(name(), Component.class);
                                getTargets(context).forEach(e -> {
                                    Score score = objective.getScoreFor(e);
                                    score.customName(component);
                                });
                                sendMessage(context, text("Custom player names updated for targets.", GREEN));
                                return 1;
                            }

                            @Override
                            public String name() {
                                return "display";
                            }

                            @Override
                            public ArgumentType<Component> type() {
                                return ArgumentTypes.component();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public String name() {
        return "name";
    }
}
