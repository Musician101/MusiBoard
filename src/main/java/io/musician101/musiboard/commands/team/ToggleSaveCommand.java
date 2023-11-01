package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ToggleSaveCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TeamArgument() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Player player = getPlayer(context);
                MusiScoreboard scoreboard = getScoreboard(player);
                Team team = TeamArgumentType.get(context);
                if (scoreboard.isTeamSaveDisabled(team)) {
                    scoreboard.enableTeamSave(team);
                    sendMessage(player, text("Team save enabled.", GREEN));
                }
                else {
                    scoreboard.disableTeamSave(team);
                    sendMessage(player, text("Team save disabled.", GREEN));
                }

                return 1;
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Toggle whether a team will be saved to a file.";
    }

    @NotNull
    @Override
    public String name() {
        return "toggleSave";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/team toggleSave <team>";
    }
}
