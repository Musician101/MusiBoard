package io.musician101.musiboard.commands.players.display;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.players.display.numberformat.NumberFormatCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class DisplayCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new NameCommand(), new NumberFormatCommand());
    }

    @Override
    public String name() {
        return "display";
    }
}
