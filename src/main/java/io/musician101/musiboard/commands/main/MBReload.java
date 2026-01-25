package io.musician101.musiboard.commands.main;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
class MBReload extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source);
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        getPlugin().reload();
        Component message = MiniMessage.miniMessage().deserialize("<green><mb-prefix>Config reloaded.", Messages.PREFIX_RESOLVER);
        context.getSource().getSender().sendMessage(message);
        return 1;
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.text("Reloads the config.");
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/mb reload");
    }
}
