package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class EnableCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "If you want to use triggers, you'll have to use the vanilla scoreboard and the /minecraft:scoreboard command.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        sendMessage(sender, text(description(sender), RED));
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "enable";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/players enable";
    }
}
