package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.EnumArgumentType;
import io.musician101.musiboard.commands.arguments.ObjectiveArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class RenderTypeCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<RenderType>() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Objective objective = ObjectiveArgumentType.get(context, "objective");
                objective.setRenderType(EnumArgumentType.get(context, name(), RenderType.class));
                sendMessage(context, text("Render type updated successfully.", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "renderType";
            }

            @NotNull
            @Override
            public ArgumentType<RenderType> type() {
                return new EnumArgumentType<>(r -> r.toString().toLowerCase(), RenderType.values());
            }
        });
    }

    @NotNull
    @Override
    public String name() {
        return "renderType";
    }
}
