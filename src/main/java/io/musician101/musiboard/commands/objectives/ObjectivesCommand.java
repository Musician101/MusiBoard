package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.objectives.modify.ModifyCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ObjectivesCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.text("Edit the objectives of the selected scoreboard.");
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new AddCommand(), new ListCommand(), new ModifyCommand(), new RemoveCommand(), new SetDisplayCommand(), new ToggleSaveCommand());
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        CommandSender sender = source.getSender();
        return canEdit(source.getSender()) && sender instanceof Player;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public String name() {
        return "objectives";
    }
}
