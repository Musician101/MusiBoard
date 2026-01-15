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
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ToggleSaveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(context);
                boolean save = !scoreboard.saveData();
                scoreboard.saveData(save);
                sendMessage(player, "<green><prefix> Scoreboard save <save-enabled:enabled:disabled>.", Formatter.booleanChoice("save-enabled", save));
                return 1;
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Toggle whether an scoreboard will be saved to a file.");
    }

    @Override
    public String name() {
        return "toggleSave";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/scoreboard toggleSave <scoreboard>");
    }
}
