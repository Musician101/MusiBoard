package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.MusiScoreboardArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
class SetGlobalCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Player player = getPlayer(context);
                Bukkit.getOnlinePlayers().forEach(p -> getManager().setScoreboard(p, scoreboard));
                sendMessage(player, text("Scoreboard globally set successfully.", GREEN));
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source.getSender());
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Set the scoreboard for all players on the server.");
    }

    @Override
    public String name() {
        return "setGlobal";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/sb setGlobal <name>");
    }
}
