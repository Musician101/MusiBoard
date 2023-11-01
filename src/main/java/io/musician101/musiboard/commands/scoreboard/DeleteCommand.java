package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.MusiScoreboardArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class DeleteCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new MusiScoreboardArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                MusiScoreboard scoreboard = getScoreboard(context);
                Bukkit.getOnlinePlayers().stream().filter(scoreboard::hasPlayer).forEach(player -> getManager().setScoreboard(player, getManager().getDefaultScoreboardOrVanilla()));
                getManager().getScoreboards().remove(scoreboard);
                sendMessage(context, text("Scoreboard deleted successfully", GREEN));
                return 1;
            }
        });
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canEdit(sender);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Delete a scoreboard.";
    }

    @NotNull
    @Override
    public String name() {
        return "delete";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/sb delete <name>";
    }
}
