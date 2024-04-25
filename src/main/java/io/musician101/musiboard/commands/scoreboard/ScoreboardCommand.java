package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScoreboardCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Manage scoreboards.";
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new CreateCommand(), new DeleteCommand(), new SetCommand(), new SetGlobalCommand(), new ToggleSaveCommand());
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @NotNull
    @Override
    public String name() {
        return "scoreboard";
    }
}
