package io.musician101.musiboard.commands.objectives;

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
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SetDisplayCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<DisplaySlot>() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                        Player player = getPlayer(context);
                        DisplaySlot displaySlot = context.getArgument("slot", DisplaySlot.class);
                        Objective objective = ObjectiveArgumentType.get(context, name());
                        setDisplaySlot(player, displaySlot, objective);
                        return 1;
                    }
                });
            }

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                setDisplaySlot((Player) context.getSource(), EnumArgumentType.get(context, name(), DisplaySlot.class), null);
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "slot";
            }

            @NotNull
            @Override
            public ArgumentType<DisplaySlot> type() {
                return new EnumArgumentType<>(DisplaySlot::getId, DisplaySlot.values());
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Set the display slot of an objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "setDisplay";
    }

    private void setDisplaySlot(@NotNull Player player, @NotNull DisplaySlot slot, @Nullable Objective objective) {
        MusiScoreboard scoreboard = getScoreboard(player);
        if (objective == null) {
            scoreboard.clearSlot(slot);
            sendMessage(player, text("Display slot cleared.", GREEN));
            return;
        }

        objective.setDisplaySlot(slot);
        sendMessage(player, text("Display slot set.", GREEN));
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/objectives setDisplay <slot> [<objective>]";
    }
}
