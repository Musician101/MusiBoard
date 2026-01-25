package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.dialog.scoreboard.ScoreboardsDialog;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
class EditCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().showDialog(ScoreboardsDialog.editDialog().build());
        return 1;
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source) && source.getSender() instanceof Player;
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Edit scoreboards.");
    }

    @Override
    public String name() {
        return "edit";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/scoreboard edit");
    }
}
