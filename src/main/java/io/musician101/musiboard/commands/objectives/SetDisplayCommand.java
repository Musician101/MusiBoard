package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SetDisplayCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<DisplaySlot>() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public int execute(@Nonnull CommandContext<CommandSender> context) {
                        Player player = getPlayer(context);
                        setDisplaySlot(player, context.getArgument("displaySlot", DisplaySlot.class), getScoreboard(player).getObjective(context.getArgument(name(), String.class)));
                        return 1;
                    }
                });
            }

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) {
                setDisplaySlot((Player) context.getSource(), EnumArgumentType.get(context, name(), DisplaySlot.class), null);
                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "slot";
            }

            @Nonnull
            @Override
            public ArgumentType<DisplaySlot> type() {
                return new EnumArgumentType<>(DisplaySlot::getId, DisplaySlot.values());
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Set the display slot of an objective.";
    }

    @Nonnull
    @Override
    public String name() {
        return "setDisplay";
    }

    private void setDisplaySlot(@Nonnull Player player, @Nonnull DisplaySlot slot, @Nullable Objective objective) {
        MusiScoreboard scoreboard = getScoreboard(player);
        if (objective == null) {
            scoreboard.clearSlot(slot);
            sendMessage(player, text("Display slot cleared.", GREEN));
            return;
        }

        objective.setDisplaySlot(slot);
        sendMessage(player, text("Display slot set.", GREEN));
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/objectives setDisplay <slot> [<objective>]";
    }
}
