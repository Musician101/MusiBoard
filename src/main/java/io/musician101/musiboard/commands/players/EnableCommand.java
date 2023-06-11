package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class EnableCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "If you want to use triggers, you'll have to use the vanilla scoreboard and the /minecraft:scoreboard command.";
    }

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        sendMessage(sender, text(description(sender), RED));
        return 1;
    }

    @Nonnull
    @Override
    public String name() {
        return "enable";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/players enable";
    }
}
