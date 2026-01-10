package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
class SetDisplayCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperArgumentCommand.AdventureFormat<DisplaySlot>() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new ObjectiveArgument() {

                    @Override
                    public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                        Player player = getPlayer(context);
                        DisplaySlot displaySlot = context.getArgument("slot", DisplaySlot.class);
                        Objective objective = ObjectiveArgumentType.get(context, name());
                        setDisplaySlot(player, displaySlot, objective);
                        return 1;
                    }
                });
            }

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                setDisplaySlot((Player) context.getSource(), EnumArgumentType.get(context, name(), DisplaySlot.class), null);
                return 1;
            }

            @Override
            public String name() {
                return "slot";
            }

            @Override
            public ArgumentType<DisplaySlot> type() {
                return new EnumArgumentType<>(DisplaySlot::getId, DisplaySlot.values());
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Set the display slot of an objective.");
    }

    @Override
    public String name() {
        return "setDisplay";
    }

    private void setDisplaySlot(Player player, DisplaySlot slot, @Nullable Objective objective) {
        MusiScoreboard scoreboard = getScoreboard(player);
        if (objective == null) {
            //TODO The way this is handled by Paper results in non-vanilla like behavior
            //TODO Objectives can only be in one display slot and clearing one display slot will clear all of them, if they're the same objective
            scoreboard.clearSlot(slot);
            sendMessage(player, text("Display slot cleared.", GREEN));
            return;
        }

        objective.setDisplaySlot(slot);
        sendMessage(player, text("Display slot set.", GREEN));
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives setDisplay <slot> [<objective>]");
    }
}
