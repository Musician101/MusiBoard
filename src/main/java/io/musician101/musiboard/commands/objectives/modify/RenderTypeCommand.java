package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class RenderTypeCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return enumCommands(RenderType.values(), (context, renderType) -> {
            Objective objective = ObjectiveArgumentType.get(context, "objective");
            objective.setRenderType(renderType);
            sendMessage(context, text("Render type updated successfully.", GREEN));
            return 1;
        });
    }

    @Override
    public String name() {
        return "renderType";
    }

    private PaperLiteralCommand.AdventureFormat renderTypeCommand(RenderType renderType) {
        return new PaperLiteralCommand.AdventureFormat() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                objective.setRenderType(renderType);
                sendMessage(context, text("Render type updated successfully.", GREEN));
                return 1;
            }

            @Override
            public String name() {
                return renderType.toString().toLowerCase();
            }
        };
    }
}
