package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.DisplayNameArgument;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.arguments.CriteriaArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

//TODO fix nullability annotations once MusiCommand gets fixed
@NullMarked
class AddCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperArgumentCommand.AdventureFormat<String>() {

            @Override
            public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                return List.of(new PaperArgumentCommand.AdventureFormat<Criteria>() {

                    @Override
                    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
                        return List.of(new DisplayNameArgument() {

                            @Override
                            public Integer execute(CommandContext<CommandSourceStack> context) {
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
                    public Integer execute(CommandContext<CommandSourceStack> context) {
                        String name = StringArgumentType.getString(context, "objective");
                        Criteria criteria = context.getArgument(name(), Criteria.class);
                        Player player = getPlayer(context);
                        registerObjective(player, name, criteria, text(name));
                        return 1;
                    }

                    @Override
                    public String name() {
                        return "criteria";
                    }

                    @Override
                    public ArgumentType<Criteria> type() {
                        return new CriteriaArgumentType();
                    }
                });
            }

            @Override
            public String name() {
                return "objective";
            }

            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Add an objective to the active scoreboard.");
    }

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

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives add <objective> <criteria> [<displayName>]");
    }
}
