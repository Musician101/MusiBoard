package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ColorCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new ColorArgument());
    }

    @Override
    public String name() {
        return "color";
    }

    private static class ColorArgument implements PaperArgumentCommand.AdventureFormat<NamedTextColor> {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Team team = TeamArgumentType.get(context);
            team.color(context.getArgument(name(), NamedTextColor.class));
            context.getSource().getSender().sendMessage(MiniMessage.miniMessage().deserialize("<green><mb-prefix>Team color updated."));
            return 1;
        }

        @Override
        public String name() {
            return "color";
        }

        @Override
        public ArgumentType<NamedTextColor> type() {
            return ArgumentTypes.namedColor();
        }
    }
}
