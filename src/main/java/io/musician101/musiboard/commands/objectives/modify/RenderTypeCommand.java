package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class RenderTypeCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return enumCommands(RenderType.values(), (context, renderType) -> {
            Objective objective = ObjectiveArgumentType.get(context, "objective");
            objective.setRenderType(renderType);
            sendMessage(context, "<green><mb-prefix>Render type updated successfully.");
            return 1;
        });
    }

    @Override
    public String name() {
        return "renderType";
    }
}
