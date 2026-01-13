package io.musician101.musiboard.commands.players.display.numberformat;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
public class NumberFormatCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperLiteralCommand.AdventureFormat() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                objective.numberFormat(NumberFormat.blank());
                sendMessage(context, "<green><mb-prefix>NumberFormat updated to BLANK.");
                return 1;
            }

            @Override
            public String name() {
                return "blank";
            }
        }, new FixedCommand(), new StyledCommand());
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source) && getPlugin().isPaperInstalled();
    }

    @Override
    public String name() {
        return "numberformat";
    }
}
