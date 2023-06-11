package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

public class ModifyCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ObjectiveArgument() {

            @Nonnull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new DisplayNameCommand(), new RenderTypeCommand());
            }
        });
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Modify the display name or render type of an objective.";
    }

    @Nonnull
    @Override
    public String name() {
        return "modify";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/objectives modify <objective> (displayName|renderType)";
    }
}
