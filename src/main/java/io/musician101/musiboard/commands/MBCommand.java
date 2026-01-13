package io.musician101.musiboard.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.CheckedBiFunction;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.musician101.musiboard.Messages.PREFIX;
import static io.musician101.musiboard.MusiBoard.getManager;

@NullMarked
public abstract class MBCommand {

    protected boolean canEdit(CommandSourceStack source) {
        return source.getSender().hasPermission("musiboard.edit");
    }

    protected Player getPlayer(CommandContext<CommandSourceStack> context) {
        return (Player) context.getSource().getSender();
    }

    protected MusiScoreboard getScoreboard(Player player) {
        return getManager().getScoreboard(player);
    }

    public void sendMessage(Audience audience, ComponentLike... components) {
        List<Component> list = new ArrayList<>();
        list.add(PREFIX);
        Arrays.stream(components).map(ComponentLike::asComponent).forEach(list::add);
        Component message = Component.join(JoinConfiguration.noSeparators(), list);
        audience.sendMessage(message);
    }

    public void sendMessage(CommandContext<CommandSourceStack> context, ComponentLike... components) {
        sendMessage(context.getSource().getSender(), components);
    }

    public static <E extends Enum<E>> List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> enumCommands(E[] values, CheckedBiFunction<CommandContext<CommandSourceStack>, E, Integer, CommandException> executor) {
        Map<E, String> nameMap = Arrays.stream(values).collect(Collectors.toMap(e -> e, Enum::toString));
        return enumCommands(values, nameMap, executor);
    }

    public static <E extends Enum<E>> List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> enumCommands(E[] values, Map<E, String> nameMap, CheckedBiFunction<CommandContext<CommandSourceStack>, E, Integer, CommandException> executor) {
        return Arrays.stream(values).map(e -> new PaperLiteralCommand.AdventureFormat() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                return executor.apply(context, e);
            }

            @Override
            public String name() {
                return nameMap.get(e);
            }
        }).collect(Collectors.toList());
    }
}
