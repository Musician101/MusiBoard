package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class RemoveCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ObjectiveArgument() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Objective objective = getObjective(context);
                objective.unregister();
                sendMessage(context, text("Objective removed.", GREEN));
                return 1;
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Remove an objective.";
    }

    @Nonnull
    @Override
    public String name() {
        return "remove";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/objectives remove <objective>";
    }
}
