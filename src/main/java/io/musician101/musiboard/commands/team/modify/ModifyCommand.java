package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.team.TeamArgument;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ModifyCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TeamArgument() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new CollisionRuleCommand(), new ColorCommand(), new DeathMessageVisibilityCommand(), new DisplayNameCommand(), new FriendlyFireCommand(), new NameTagVisibilityCommand(), new PrefixCommand(), new SeeFriendlyInvisiblesCommand(), new SuffixCommand());
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Modifies the options of a team.");
    }

    @Override
    public String name() {
        return "modify";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team modify <team> <option> <value>");
    }
}
