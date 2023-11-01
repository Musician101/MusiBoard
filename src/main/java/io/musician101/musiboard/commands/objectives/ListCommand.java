package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;

class ListCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "List the objectives for the active scoreboard.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = getPlayer(context);
        Set<Objective> objectives = getScoreboard(player).getObjectives();
        Component joined = textOfChildren(text(objectives.size() == 0 ? "" : ":"), join(JoinConfiguration.commas(true), objectives.stream().map(Objective::displayName).toList()));
        Component first = text("There are " + objectives.size() + " objective" + (objectives.size() == 1 ? "" : "s"));
        sendMessage(player, first, joined);
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
        return "/objectives list";
    }
}
