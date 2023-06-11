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
import java.util.List;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

class AddCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<String>() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new ArgumentCommand<Criteria>() {

                    @Nonnull
                    @Override
                    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                        return List.of(new DisplayNameArgument() {

                            @Override
                            public int execute(@Nonnull CommandContext<CommandSender> context) {
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
                    public int execute(@Nonnull CommandContext<CommandSender> context) {
                        String name = StringArgumentType.getString(context, "objective");
                        Criteria criteria = context.getArgument(name(), Criteria.class);
                        Player player = getPlayer(context);
                        registerObjective(player, name, criteria, text(name));
                        return 1;
                    }

                    @Nonnull
                    @Override
                    public String name() {
                        return "criteria";
                    }

                    @Nonnull
                    @Override
                    public ArgumentType<Criteria> type() {
                        return new CriteriaArgumentType();
                    }
                });
            }

            @Nonnull
            @Override
            public String name() {
                return "objective";
            }

            @Nonnull
            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Add an objective to the active scoreboard.";
    }

    @Nonnull
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

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/objectives add <objective> <criteria> [<displayName>]";
    }
}
