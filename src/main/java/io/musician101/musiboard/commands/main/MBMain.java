package io.musician101.musiboard.commands.main;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class MBMain extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new MBReload(), new MBSave());
    }

    private void commandInfo(CommandSourceStack source, PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> cmd) {
        cmd.children().stream().filter(child -> child.canUse(source)).forEach(child -> {
            String string = "<click:suggest_command:/" + cmd.name() + ">/" + cmd.name() + " <dark_gray>- <gray>" + cmd.description(source);
            source.getSender().sendMessage(MiniMessage.miniMessage().deserialize(string));
        });
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        commandInfo(source, this);
        commandInfo(source, new ScoreboardCommand());
        return 1;
    }

    @Override
    public String name() {
        return "musiboard";
    }
}
