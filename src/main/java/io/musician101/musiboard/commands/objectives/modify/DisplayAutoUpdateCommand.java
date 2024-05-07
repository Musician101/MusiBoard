package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class DisplayAutoUpdateCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<Boolean>() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                objective.setAutoUpdateDisplay(BoolArgumentType.getBool(context, name()));
                sendMessage(context, text("AutoDisplayUpdate has been updated.", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "boolean";
            }

            @NotNull
            @Override
            public ArgumentType<Boolean> type() {
                return BoolArgumentType.bool();
            }
        });
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canEdit(sender) && getPlugin().isPaperInstalled();
    }

    @NotNull
    @Override
    public String name() {
        return "displayautoupdate";
    }
}
