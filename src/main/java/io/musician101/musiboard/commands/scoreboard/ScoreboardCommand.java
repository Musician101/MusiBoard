package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

public class ScoreboardCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new CreateCommand(), new DeleteCommand(), new SetCommand(), new SetGlobalCommand(), new ToggleSaveCommand());
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Nonnull
    @Override
    public String name() {
        return "scoreboard";
    }
}
