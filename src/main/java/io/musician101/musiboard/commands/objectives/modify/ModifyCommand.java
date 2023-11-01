package io.musician101.musiboard.commands.objectives.modify;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import io.musician101.musiboard.commands.ObjectiveArgument;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ModifyCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ObjectiveArgument() {

            @NotNull
            @Override
            public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
                return List.of(new DisplayNameCommand(), new RenderTypeCommand());
            }
        });
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Modify the display name or render type of an objective.";
    }

    @NotNull
    @Override
    public String name() {
        return "modify";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/objectives modify <objective> (displayName|renderType)";
    }
}
