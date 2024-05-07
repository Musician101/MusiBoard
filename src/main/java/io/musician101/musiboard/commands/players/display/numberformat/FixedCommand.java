package io.musician101.musiboard.commands.players.display.numberformat;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.ComponentArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class FixedCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<Component>() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                Component contents = ComponentArgumentType.get(context, name());
                objective.numberFormat(NumberFormat.fixed(contents));
                sendMessage(context, text("Fixed format updated successfully.", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "contents";
            }

            @NotNull
            @Override
            public ArgumentType<Component> type() {
                return new ComponentArgumentType();
            }
        });
    }

    @NotNull
    @Override
    public String name() {
        return "fixed";
    }
}
