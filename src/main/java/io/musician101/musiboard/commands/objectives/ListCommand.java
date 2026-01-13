package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
class ListCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("List the objectives for the active scoreboard.");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        Player player = getPlayer(context);
        Set<Objective> objectives = getScoreboard(player).getObjectives();
        Component joined = objectives.stream().map(Objective::displayName).collect(Component.toComponent(Component.text(", ")));
        String message = "There are " + objectives.size() + " objective" + (objectives.size() == 1 ? "" : "s") + (objectives.isEmpty() ? "" : ":<newline><objectives>");
        sendMessage(player, message, TagResolver.resolver("objectives", Tag.selfClosingInserting(joined)));
        return 1;
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives list");
    }
}
