package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
public class EnableCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("If you want to use triggers, you'll have to use the vanilla scoreboard and the /minecraft:scoreboard command.");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        sendMessage(source.getSender(), description(source).asComponent().color(RED));
        return 1;
    }

    @Override
    public String name() {
        return "enable";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/players enable");
    }
}
