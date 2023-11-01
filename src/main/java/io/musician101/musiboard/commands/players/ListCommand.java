package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class ListCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                return getTarget(context).map(entity -> {
                    Set<Score> scores = scoreboard.getScores(entity.getName());
                    if (scores.isEmpty()) {
                        sendMessage(player, text(entity.getName() + " has no scores to show.", GREEN));
                    }
                    else {
                        sendMessage(player, text(entity.getName() + " has " + scores.size() + " score(s): ", GREEN));
                        scores.forEach(score -> player.sendMessage(textOfChildren(score.getObjective().displayName(), text(": " + score.getScore()))));
                    }

                    return 1;
                }).orElseGet(() -> {
                    sendMessage(player, text("Provided target has no scores to show.", RED));
                    return 1;
                });
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "List tracked entities or the scores of a specific entity on the active scoreboard.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = getPlayer(context);
        Set<String> entries = getScoreboard(player).getEntries();
        if (entries.isEmpty()) {
            sendMessage(player, text("There are no tracked entities.", GREEN));
        }
        else {
            int size = entries.size();
            Component entriesList = join(JoinConfiguration.separator(text(", ", GRAY)), entries.stream().map(s -> text(s, GREEN)).toList());
            sendMessage(player, text("There " + (size == 1 ? "is " : "are ") + size + " tracked entit" + (size == 1 ? "y" : "ies") + ":"), entriesList);
        }

        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "list";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/players list [<target>]";
    }
}
