package io.musician101.musiboard.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.CheckedBiFunction;
import io.musician101.musiboard.Messages;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NullMarked
public abstract class MBCommand {

    protected boolean canEdit(CommandSourceStack source) {
        return source.getSender().hasPermission("musiboard.edit");
    }

    protected Player getPlayer(CommandContext<CommandSourceStack> context) {
        return (Player) context.getSource().getSender();
    }

    public void sendMessage(Audience audience, String text, TagResolver... resolvers) {
        List<TagResolver> resolverList = Arrays.asList(resolvers);
        resolverList.add(Messages.PREFIX_RESOLVER);
        audience.sendMessage(MiniMessage.miniMessage().deserialize(text, resolvers));
    }

    public void sendMessage(CommandContext<CommandSourceStack> context, String text, TagResolver... resolvers) {
        sendMessage(context.getSource().getSender(), text, resolvers);
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
