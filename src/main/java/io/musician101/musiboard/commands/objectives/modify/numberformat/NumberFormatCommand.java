package io.musician101.musiboard.commands.objectives.modify.numberformat;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class NumberFormatCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new LiteralCommand() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                objective.numberFormat(NumberFormat.blank());
                sendMessage(context, text("NumberFormat updated to BLANK.", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "blank";
            }
        }, new FixedCommand(), new StyledCommand());
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canEdit(sender) && getPlugin().isPaperInstalled();
    }

    @NotNull
    @Override
    public String name() {
        return "numberformat";
    }
}
