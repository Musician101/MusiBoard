package io.musician101.musiboard.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.bukkitier.command.help.HelpMainCommand;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.MusiBoard;
import io.musician101.musiboard.commands.objectives.ObjectivesCommand;
import io.musician101.musiboard.commands.players.PlayersCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.commands.team.TeamCommand;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class MBCommand extends HelpMainCommand {

    public MBCommand() {
        super(MusiBoard.getPlugin());
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new LiteralCommand() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) {
                MusiBoard.getPlugin().reload();
                context.getSource().sendMessage(textOfChildren(Messages.PREFIX, text("Config reloaded.", GREEN)));
                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "reload";
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void commandInfo(CommandSender sender, Command<? extends ArgumentBuilder<CommandSender, ?>> mbcmd) {
        mbcmd.arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> sender.spigot().sendMessage(commandInfo(cmd, sender)));
    }

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        super.execute(context);
        CommandSender sender = context.getSource();
        commandInfo(sender, new ObjectivesCommand());
        commandInfo(sender, new PlayersCommand());
        commandInfo(sender, new ScoreboardCommand());
        commandInfo(sender, new TeamCommand());
        return 1;
    }

    @Nonnull
    @Override
    public String name() {
        return "musiboard";
    }
}
