package io.musician101.musiboard.commands.players;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.players.display.DisplayCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class PlayersCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Edit the player settings of the selected scoreboard.");
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new AddCommand(), new DisplayCommand(), new EnableCommand(), new GetCommand(), new ListCommand(), new OperationCommand(), new RemoveCommand(), new ResetCommand(), new SetCommand());
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source.getSender()) && source.getSender() instanceof Player;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public String name() {
        return "players";
    }
}
