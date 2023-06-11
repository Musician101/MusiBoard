package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class EmptyCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TeamArgument() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                team.removeEntries(team.getEntries());
                sendMessage(context, text("Removed all members from ", GREEN), team.displayName());
                return 1;
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Empties a team of all members.";
    }

    @Nonnull
    @Override
    public String name() {
        return "remove";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/team empty <team>";
    }
}
