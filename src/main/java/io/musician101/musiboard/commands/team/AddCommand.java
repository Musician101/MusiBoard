package io.musician101.musiboard.commands.team;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.DisplayNameArgument;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class AddCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<String>() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new DisplayNameArgument() {

                    @Override
                    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                        createTeam(getPlayer(context), StringArgumentType.getString(context, name()), context.getArgument(name(), Component.class));
                        return 1;
                    }
                });
            }

            @NotNull
            @Override
            public String name() {
                return "team";
            }

            @NotNull
            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    private void createTeam(Player player, String name, Component displayName) throws CommandSyntaxException {
        MusiScoreboard scoreboard = getScoreboard(player);
        Team t = scoreboard.getTeam(name);
        if (t == null) {
            throw new SimpleCommandExceptionType(() -> "A team with that name already exists.").create();
        }

        Team team = scoreboard.registerNewTeam(name);
        team.displayName(displayName);
        sendMessage(player, text("Team created successfully.", GREEN));
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Create a new team.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, name());
        createTeam(getPlayer(context), name, text(name));
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "add";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/team add <team> [<displayName>]";
    }
}
