package io.musician101.musiboard.commands.players.display;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.players.display.numberformat.NumberFormatCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DisplayCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new NameCommand(), new NumberFormatCommand());
    }

    @NotNull
    @Override
    public String name() {
        return "display";
    }
}
