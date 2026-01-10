package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
public class ListCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TargetArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
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

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("List tracked entities or the scores of a specific entity on the active scoreboard.");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
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

    @Override
    public String name() {
        return "list";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players list [<target>]");
    }
}
