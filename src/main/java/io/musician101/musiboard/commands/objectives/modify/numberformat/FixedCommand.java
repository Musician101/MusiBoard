package io.musician101.musiboard.commands.objectives.modify.numberformat;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class FixedCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperArgumentCommand.AdventureFormat<Component>() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                Component contents = context.getArgument(name(), Component.class);
                objective.numberFormat(NumberFormat.fixed(contents));
                sendMessage(context, "<green><mb-prefix>Fixed format updated successfully.");
                return 1;
            }

            @Override
            public String name() {
                return "contents";
            }

            @Override
            public ArgumentType<Component> type() {
                return ArgumentTypes.component();
            }
        });
    }

    @Override
    public String name() {
        return "fixed";
    }
}
