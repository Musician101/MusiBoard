package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.ComponentArgumentType;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import java.util.List;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class PrefixCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<Component>() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                team.prefix(ComponentArgumentType.get(context, name()));
                sendMessage(context, text("Prefix updated successfully.", GREEN));
                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "prefix";
            }

            @Nonnull
            @Override
            public ArgumentType<Component> type() {
                return new ComponentArgumentType();
            }
        });
    }

    @Nonnull
    @Override
    public String name() {
        return "prefix";
    }
}
