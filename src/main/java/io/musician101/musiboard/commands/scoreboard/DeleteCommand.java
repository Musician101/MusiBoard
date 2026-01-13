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
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
class DeleteCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Bukkit.getOnlinePlayers().stream().filter(scoreboard::hasPlayer).forEach(player -> getManager().setScoreboard(player, getManager().getDefaultScoreboardOrVanilla()));
                getManager().getScoreboards().remove(scoreboard);
                sendMessage(context, "<green><mb-prefix>Scoreboard deleted successfully");
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source);
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Delete a scoreboard.");
    }

    @Override
    public String name() {
        return "delete";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/sb delete <name>");
    }
}
