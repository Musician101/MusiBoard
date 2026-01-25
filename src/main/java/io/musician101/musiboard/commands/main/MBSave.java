package io.musician101.musiboard.commands.main;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

import static io.musician101.musiboard.MusiBoard.getManager;
import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
class MBSave extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source);
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.text("Manually saves the scoreboards.");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        try {
            getManager().save();
            sendMessage(context, "<green><mb-prefix> Scoreboards successfully saved.", Messages.PREFIX_RESOLVER);
        }
        catch (IOException e) {
            sendMessage(context, "<red><mb-prefix> An error occurred while attempting to save scoreboards. Check the console for more info.");
            getPlugin().getComponentLogger().error(Component.text("An error occurred while saving scoreboards.", NamedTextColor.RED), e);
        }

        return 1;
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/mb save");
    }
}
