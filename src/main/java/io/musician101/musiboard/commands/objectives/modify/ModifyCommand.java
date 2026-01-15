package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import io.musician101.musiboard.commands.objectives.modify.numberformat.NumberFormatCommand;
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
        return List.of(ObjectiveArgument.withChildren(new DisplayAutoUpdateCommand(), new DisplayNameCommand(), new NumberFormatCommand(), new RenderTypeCommand()));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Modify the display name or render type of an objective.");
    }

    @Override
    public String name() {
        return "modify";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives modify <objective> (displayName|renderType)");
    }
}
