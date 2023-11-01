package io.musician101.musiboard.commands.team.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.players.TargetArgument;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ModifyCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new TargetArgument() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new CollisionRuleCommand(), new ColorCommand(), new DeathMessageVisibilityCommand(), new DisplayNameCommand(), new FriendlyFireCommand(), new NameTagVisibilityCommand(), new PrefixCommand(), new SeeFriendlyInvisiblesCommand(), new SuffixCommand());
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Modifies the options of a team.";
    }

    @NotNull
    @Override
    public String name() {
        return "modify";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/team modify <team> <option> <value>";
    }
}
