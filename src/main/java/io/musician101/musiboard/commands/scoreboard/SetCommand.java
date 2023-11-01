package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.MusiScoreboardArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SetCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Player player = getPlayer(context);
                getManager().setScoreboard(player, scoreboard);
                sendMessage(player, text("Scoreboard set successfully.", GREEN));
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canOverride(sender);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Set your scoreboard.";
    }

    @NotNull
    @Override
    public String name() {
        return "set";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/sb set <name>";
    }
}
