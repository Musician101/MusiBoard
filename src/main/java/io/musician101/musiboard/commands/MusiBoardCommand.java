package io.musician101.musiboard.commands;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import javax.annotation.Nonnull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.Component.textOfChildren;

public abstract class MusiBoardCommand {

    protected boolean canEdit(@Nonnull CommandSender sender) {
        return sender.hasPermission("musiboard.edit");
    }

    protected boolean canOverride(@Nonnull CommandSender sender) {
        return sender.hasPermission("musiboard.override") && sender instanceof Player;
    }

    @Nonnull
    protected MusiScoreboardManager getManager() {
        return getPlugin().getManager();
    }

    @Nonnull
    protected Player getPlayer(@Nonnull CommandContext<CommandSender> context) {
        return (Player) context.getSource();
    }

    @Nonnull
    protected MusiScoreboard getScoreboard(@Nonnull Player player) {
        return getManager().getScoreboard(player);
    }

    public void sendMessage(Audience audience, ComponentLike... components) {
        audience.sendMessage(textOfChildren(Messages.PREFIX, textOfChildren(components)));
    }

    public void sendMessage(CommandContext<CommandSender> context, ComponentLike... components) {
        sendMessage(context.getSource(), components);
    }
}
