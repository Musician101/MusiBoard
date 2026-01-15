package io.musician101.musiboard.commands.objectives;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.DisplayNameArgument;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getScoreboard;

@NullMarked
class AddCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new ObjectiveArgument());
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Add an objective to the active scoreboard.");
    }

    @Override
    public String name() {
        return "add";
    }

    private static void registerObjective(Player player, String name, Criteria criteria, Component displayName) {
        MusiScoreboard scoreboard = getScoreboard(player);
        TagResolver objectiveResolver = TagResolver.resolver("objective", Tag.selfClosingInserting(displayName));
        if (scoreboard.getObjective(name) == null) {
            scoreboard.registerNewObjective(name, criteria, displayName);
            Component message = MiniMessage.miniMessage().deserialize("<green><mb-prefix>Objective <objective> <green>registered.", objectiveResolver);
            player.sendMessage(message);
            return;
        }

        Component component = MiniMessage.miniMessage().deserialize("<red><mb-prefix>Objective <objective> already exists.", objectiveResolver);
        player.sendMessage(component);
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/objectives add <objective> <criteria> [<displayName>]");
    }

    private class ObjectiveArgument implements PaperArgumentCommand.AdventureFormat<String> {

        @Override
        public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
            return List.of(new CriteriaArgument());
        }

        @Override
        public String name() {
            return "objective";
        }

        @Override
        public ArgumentType<String> type() {
            return StringArgumentType.word();
        }
    }

    private class CriteriaArgument implements PaperArgumentCommand.AdventureFormat<Criteria> {

        @Override
        public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
            return List.of(DisplayNameArgument.withExecutor(context -> {
                String name = StringArgumentType.getString(context, "objective");
                Criteria criteria = context.getArgument("criteria", Criteria.class);
                Component displayName = DisplayNameArgument.get(context);
                Player player = getPlayer(context);
                registerObjective(player, name, criteria, displayName);
                return 1;
            }));
        }

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            String name = StringArgumentType.getString(context, "objective");
            Criteria criteria = context.getArgument(name(), Criteria.class);
            Player player = getPlayer(context);
            registerObjective(player, name, criteria, Component.text(name));
            return 1;
        }

        @Override
        public String name() {
            return "criteria";
        }

        @Override
        public ArgumentType<Criteria> type() {
            return ArgumentTypes.objectiveCriteria();
        }
    }
}
