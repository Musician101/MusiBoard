package io.musician101.musiboard.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.Messages;
import io.musician101.musiboard.commands.objectives.ObjectivesCommand;
import io.musician101.musiboard.commands.players.PlayersCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.commands.team.TeamCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getPlugin;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public class MBMain extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MBReload());
    }

    private void commandInfo(CommandSourceStack source, PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> mbcmd) {
        String string = "<click:suggest_command:/" + mbcmd.name() + ">/" + mbcmd.name() + " <dark_gray>- <gray>" + mbcmd.description(source);
        source.getSender().sendMessage(miniMessage().deserialize(string));
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        commandInfo(source, new ObjectivesCommand());
        commandInfo(source, new PlayersCommand());
        commandInfo(source, new ScoreboardCommand());
        commandInfo(source, new TeamCommand());
        return 1;
    }

    @Override
    public String name() {
        return "musiboard";
    }

    private static class MBReload extends MBCommand implements PaperLiteralCommand.AdventureFormat {

        @Override
        public boolean canUse(CommandSourceStack source) {
            return canEdit(source);
        }

        @Override
        public String name() {
            return "reload";
        }

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            getPlugin().reload();
            Component message = MiniMessage.miniMessage().deserialize("<green><mb-prefix>Config reloaded.", Messages.PREFIX_RESOLVER);
            context.getSource().getSender().sendMessage(message);
            return 1;
        }

        @Override
        public ComponentLike description(CommandSourceStack sender) {
            return Component.text("Reloads the config.");
        }
    }
}
