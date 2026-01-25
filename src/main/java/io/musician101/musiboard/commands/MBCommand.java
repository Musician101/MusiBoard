package io.musician101.musiboard.commands;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;

@NullMarked
public abstract class MBCommand {

    protected boolean canEdit(CommandSourceStack source) {
        return source.getSender().hasPermission("musiboard.edit");
    }

    public void sendMessage(Audience audience, String text, TagResolver... resolvers) {
        List<TagResolver> resolverList = Arrays.asList(resolvers);
        resolverList.add(Messages.PREFIX_RESOLVER);
        audience.sendMessage(MiniMessage.miniMessage().deserialize(text, resolvers));
    }

    public void sendMessage(CommandContext<CommandSourceStack> context, String text, TagResolver... resolvers) {
        sendMessage(context.getSource().getSender(), text, resolvers);
    }
}
