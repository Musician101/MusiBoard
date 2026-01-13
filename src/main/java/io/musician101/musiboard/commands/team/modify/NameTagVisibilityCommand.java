package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public class NameTagVisibilityCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return OptionStatusArgument.get(((context, optionStatus) -> {
            Team team = TeamArgumentType.get(context);
            team.setOption(Option.NAME_TAG_VISIBILITY, optionStatus);
            sendMessage(context, text("Name tag visibility updated successfully."));
            return 1;
        }));
    }

    @Override
    public String name() {
        return "nameTagVisibility";
    }
}
