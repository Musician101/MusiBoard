package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.MusiScoreboardArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ToggleSaveCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(context);
                boolean save = !scoreboard.saveData();
                scoreboard.saveData(save);
                sendMessage(player, text("Scoreboard save " + (save ? "en" : "dis") + "abled.", GREEN));
                return 1;
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Toggle whether an scoreboard will be saved to a file.";
    }

    @NotNull
    @Override
    public String name() {
        return "toggleSave";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/scoreboard toggleSave <scoreboard>";
    }
}
