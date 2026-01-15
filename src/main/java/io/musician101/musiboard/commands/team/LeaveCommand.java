package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.EntitiesArgumentType;
import io.musician101.musiboard.commands.players.TargetArgument;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
public class LeaveCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MembersArgument());
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Makes the specified entities leave their teams.");
    }

    @Override
    public String name() {
        return "leave";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team leave <members>");
    }

    private static class MembersArgument extends TargetArgument {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            List<Entity> targets = EntitiesArgumentType.getTargets(context, name());
            Player player = getPlayer(context);
            MusiScoreboard scoreboard = getScoreboard(player);
            scoreboard.getTeams().forEach(team -> targets.forEach(team::removeEntity));
            sendMessage(player, "<green><mb-prefix>Removed " + targets.size() + " member(s) from team(s).");
            return 1;
        }

        @Override
        public String name() {
            return "members";
        }
    }
}
