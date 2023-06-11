package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.players.TargetArgument;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

public class ModifyCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new CollisionRuleCommand(), new ColorCommand(), new DeathMessageVisibilityCommand(), new DisplayNameCommand(), new FriendlyFireCommand(), new NameTagVisibilityCommand(), new PrefixCommand(), new SeeFriendlyInvisiblesCommand(), new SuffixCommand());
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Modifies the options of a team.";
    }

    @Nonnull
    @Override
    public String name() {
        return "modify";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/team modify <team> <option> <value>";
    }
}
