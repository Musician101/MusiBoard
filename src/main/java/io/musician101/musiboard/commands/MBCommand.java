package io.musician101.musiboard.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.bukkitier.command.help.HelpMainCommand;
import io.musician101.musiboard.commands.objectives.ObjectivesCommand;
import io.musician101.musiboard.commands.players.PlayersCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.commands.team.TeamCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.musician101.musiboard.Messages.PREFIX;
import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class MBCommand extends HelpMainCommand {

    public MBCommand() {
        super(getPlugin());
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new LiteralCommand() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
                getPlugin().reload();
                context.getSource().sendMessage(textOfChildren(PREFIX, text("Config reloaded.", GREEN)));
                return 1;
            }

            @NotNull
            @Override
            public String description(@NotNull CommandSender sender) {
                return "Reloads the config.";
            }

            @NotNull
            @Override
            public String name() {
                return "reload";
            }
        });
    }

    private void commandInfo(CommandSender sender, Command<? extends ArgumentBuilder<CommandSender, ?>> mbcmd) {
        String string = "<click:suggest_command:/" + mbcmd.name() + ">/" + mbcmd.name() + " <dark_gray>- <gray>" + mbcmd.description(sender);
        sender.sendMessage(miniMessage().deserialize(string));
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        super.execute(context);
        CommandSender sender = context.getSource();
        commandInfo(sender, new ObjectivesCommand());
        commandInfo(sender, new PlayersCommand());
        commandInfo(sender, new ScoreboardCommand());
        commandInfo(sender, new TeamCommand());
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "musiboard";
    }
}
