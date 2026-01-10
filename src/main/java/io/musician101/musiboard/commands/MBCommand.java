package io.musician101.musiboard.commands;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static io.musician101.musiboard.Messages.PREFIX;
import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.Component.textOfChildren;

@NullMarked
public abstract class MBCommand {

    protected boolean canEdit(CommandSender sender) {
        return sender.hasPermission("musiboard.edit");
    }

    protected boolean canOverride(CommandSender sender) {
        return sender.hasPermission("musiboard.override") && sender instanceof Player;
    }

    protected MusiScoreboardManager getManager() {
        return getPlugin().getManager();
    }

    protected Player getPlayer(CommandContext<CommandSourceStack> context) {
        return (Player) context.getSource().getSender();
    }

    protected MusiScoreboard getScoreboard(Player player) {
        return getManager().getScoreboard(player);
    }

    public void sendMessage(Audience audience, ComponentLike... components) {
        audience.sendMessage(textOfChildren(PREFIX, textOfChildren(components)));
    }

    public void sendMessage(CommandContext<CommandSourceStack> context, ComponentLike... components) {
        sendMessage(context.getSource().getSender(), components);
    }
}
