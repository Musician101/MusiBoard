package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.team.modify.ModifyCommand;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new AddCommand(), new EmptyCommand(), new JoinCommand(), new LeaveCommand(), new ListCommand(), new ModifyCommand(), new RemoveCommand(), new ToggleSaveCommand());
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canEdit(sender) && sender instanceof Player;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @NotNull
    @Override
    public String name() {
        return "team";
    }
}
