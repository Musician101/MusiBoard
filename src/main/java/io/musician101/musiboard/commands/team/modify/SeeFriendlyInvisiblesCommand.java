package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class SeeFriendlyInvisiblesCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new SeeFriendlyInvisiblesArgument());
    }

    @Override
    public String name() {
        return "seeFriendlyInvisibles";
    }

    private class SeeFriendlyInvisiblesArgument implements PaperArgumentCommand.AdventureFormat<Boolean> {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Team team = TeamArgumentType.get(context);
            team.setCanSeeFriendlyInvisibles(BoolArgumentType.getBool(context, name()));
            sendMessage(context, "<green><mb-prefix> Setting updated successfully.");
            return 1;
        }

        @Override
        public String name() {
            return "seeFriendlyInvisibles";
        }

        @Override
        public ArgumentType<Boolean> type() {
            return BoolArgumentType.bool();
        }
    }
}
