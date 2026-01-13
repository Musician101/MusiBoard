package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class PrefixCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperArgumentCommand.AdventureFormat<Component>() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                Team team = TeamArgumentType.get(context);
                team.prefix(context.getArgument(name(), Component.class));
                sendMessage(context, "<green><mb-prefix>Prefix updated successfully.");
                return 1;
            }

            @Override
            public String name() {
                return "prefix";
            }

            @Override
            public ArgumentType<Component> type() {
                return ArgumentTypes.component();
            }
        });
    }

    @Override
    public String name() {
        return "prefix";
    }
}
