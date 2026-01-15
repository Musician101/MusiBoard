package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.DisplayNameArgument;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getScoreboard;
import static net.kyori.adventure.text.Component.text;

@NullMarked
public class AddCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new TeamArgument());
    }

    private static void createTeam(Player player, String name, Component displayName) throws CommandException {
        MusiScoreboard scoreboard = getScoreboard(player);
        Team t = scoreboard.getTeam(name);
        if (t == null) {
            throw new CommandException("A team with that name already exists.");
        }

        Team team = scoreboard.registerNewTeam(name);
        team.displayName(displayName);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green><mb-prefix>Team created successfully."));
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Create a new team.");
    }

    @Override
    public String name() {
        return "add";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/team add <team> [<displayName>]");
    }

    private static class TeamArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<String> {

        @Override
        public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
            return List.of(DisplayNameArgument.withExecutor(context -> {
                createTeam(getPlayer(context), StringArgumentType.getString(context, name()), context.getArgument(name(), Component.class));
                return 1;
            }));
        }

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
            String name = StringArgumentType.getString(context, name());
            createTeam(getPlayer(context), name, text(name));
            return 1;
        }

        @Override
        public String name() {
            return "team";
        }

        @Override
        public ArgumentType<String> type() {
            return StringArgumentType.word();
        }
    }
}
