package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.arguments.TeamArgumentType;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class FriendlyFireCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<Boolean>() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                team.setAllowFriendlyFire(BoolArgumentType.getBool(context, name()));
                sendMessage(context, text("Allow friendly fire updated.", GREEN));
                return 1;
            }

            @NotNull
            @Override
            public String name() {
                return "friendlyFire";
            }

            @NotNull
            @Override
            public ArgumentType<Boolean> type() {
                return BoolArgumentType.bool();
            }
        });
    }

    @NotNull
    @Override
    public String name() {
        return "friendlyFire";
    }
}
