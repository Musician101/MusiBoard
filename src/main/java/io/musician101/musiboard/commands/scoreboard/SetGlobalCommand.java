package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.MusiScoreboardArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SetGlobalCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Player player = getPlayer(context);
                Bukkit.getOnlinePlayers().forEach(p -> getManager().setScoreboard(p, scoreboard));
                sendMessage(player, text("Scoreboard globally set successfully.", GREEN));
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canEdit(sender);
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Set the scoreboard for all players on the server.";
    }

    @Nonnull
    @Override
    public String name() {
        return "setGlobal";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/sb setGlobal <name>";
    }
}
