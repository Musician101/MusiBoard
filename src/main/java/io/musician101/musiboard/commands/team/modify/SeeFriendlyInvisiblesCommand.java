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
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;

public class SeeFriendlyInvisiblesCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<Boolean>() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
                Team team = TeamArgumentType.get(context);
                team.setCanSeeFriendlyInvisibles(BoolArgumentType.getBool(context, name()));
                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "seeFriendlyInvisibles";
            }

            @Nonnull
            @Override
            public ArgumentType<Boolean> type() {
                return BoolArgumentType.bool();
            }
        });
    }

    @Nonnull
    @Override
    public String name() {
        return "seeFriendlyInvisibles";
    }
}
