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
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
class SetCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Player player = getPlayer(context);
                getManager().setScoreboard(player, scoreboard);
                sendMessage(player, text("Scoreboard set successfully.", GREEN));
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return source.getSender().hasPermission("musiboard.override") && source instanceof Player;
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Set your scoreboard.");
    }

    @Override
    public String name() {
        return "set";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/sb set <name>");
    }
}
