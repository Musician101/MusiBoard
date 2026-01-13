package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

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
                        sendMessage(player, "<green><mb-prefix> " + entity.getName() + " has no scores to show.");
                    }
                    else {
                        sendMessage(player, "<green><prefix> " + entity.getName() + " has " + scores.size() + " score(s): ");
                        scores.forEach(score -> {
                            String message = "<objective: " + score.getScore();
                            sendMessage(player, message, Messages.objectiveResolver(score.getObjective()));
                        });
                    }

                    return 1;
                }).orElseGet(() -> {
                    sendMessage(player, "<red><mb-prefix>Provided target has no scores to show.");
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
            sendMessage(player, "<green><mb-prefix>There are no tracked entities.");
        }
        else {
            int size = entries.size();
            String message = "<mb-prefix> There " + (size == 1 ? "is " : "are ") + size + " tracked entit" + (size == 1 ? "y" : "ies") + ":<entries>";
            Component entriesList = Component.join(JoinConfiguration.separator(text(", ", GRAY)), entries.stream().map(s -> text(s, GREEN)).toList());
            TagResolver resolver = TagResolver.resolver("entries", Tag.selfClosingInserting(entriesList));
            sendMessage(player, message, resolver);
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
