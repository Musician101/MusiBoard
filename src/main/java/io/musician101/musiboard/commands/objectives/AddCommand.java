package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.DisplayNameArgument;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.CriteriaArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

class AddCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<String>() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ArgumentCommand<Criteria>() {

                    @NotNull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new DisplayNameArgument() {

                            @Override
                            public int execute(@NotNull CommandContext<CommandSender> context) {
                                String name = StringArgumentType.getString(context, "objective");
                                Criteria criteria = context.getArgument("criteria", Criteria.class);
                                Component displayName = get(context);
                                Player player = getPlayer(context);
                                registerObjective(player, name, criteria, displayName);
                                return 1;
                            }
                        });
                    }

                    @Override
                    public int execute(@NotNull CommandContext<CommandSender> context) {
                        String name = StringArgumentType.getString(context, "objective");
                        Criteria criteria = context.getArgument(name(), Criteria.class);
                        Player player = getPlayer(context);
                        registerObjective(player, name, criteria, text(name));
                        return 1;
                    }

                    @NotNull
                    @Override
                    public String name() {
                        return "criteria";
                    }

                    @NotNull
                    @Override
                    public ArgumentType<Criteria> type() {
                        return new CriteriaArgumentType();
                    }
                });
            }

            @NotNull
            @Override
            public String name() {
                return "objective";
            }

            @NotNull
            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Add an objective to the active scoreboard.";
    }

    @NotNull
    @Override
    public String name() {
        return "add";
    }

    private void registerObjective(Player player, String name, Criteria criteria, Component displayName) {
        MusiScoreboard scoreboard = getScoreboard(player);
        if (scoreboard.getObjective(name) == null) {
            scoreboard.registerNewObjective(name, criteria, displayName);
            sendMessage(player, text("Objective ", GREEN), displayName, text(" registered.", GREEN));
            return;
        }

        sendMessage(player, text("Objective ", RED), displayName, text(" already exists.", RED));
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/objectives add <objective> <criteria> [<displayName>]";
    }
}
