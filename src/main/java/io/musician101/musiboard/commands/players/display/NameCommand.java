package io.musician101.musiboard.commands.players.display;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.ComponentArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class NameCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Override
            public @NotNull List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public @NotNull List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new ArgumentCommand<Component>() {

                            @Override
                            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                                Objective objective = getObjective(context);
                                Component component = ComponentArgumentType.get(context, name());
                                getTargets(context).forEach(e -> {
                                    Score score = objective.getScoreFor(e);
                                    score.customName(component);
                                });
                                sendMessage(context, text("Custom player names updated for targets.", GREEN));
                                return 1;
                            }

                            @NotNull
                            @Override
                            public String name() {
                                return "display";
                            }

                            @NotNull
                            @Override
                            public ArgumentType<Component> type() {
                                return new ComponentArgumentType();
                            }
                        });
                    }
                });
            }
        });
    }

    @NotNull
    @Override
    public String name() {
        return "name";
    }
}
