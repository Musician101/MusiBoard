package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;

@NullMarked
class CreateCommand extends MBCommand implements PaperLiteralCommand.AdventureFormat {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new PaperArgumentCommand.AdventureFormat<String>() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) {
                String name = StringArgumentType.getString(context, name());
                boolean success = getManager().registerNewScoreboard(name);
                CommandSender sender = context.getSource().getSender();
                if (success) {
                    sendMessage(sender, "<green><mb-prefix> " + name + " created successfully.");
                }
                else {
                    sendMessage(sender, "<red><mb-prefix> " + name + " already exists.");
                }

                return 1;
            }

            @Override
            public String name() {
                return "name";
            }

            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @Override
    public boolean canUse(CommandSourceStack source) {
        return canEdit(source);
    }

    @Override
    public ComponentLike description(CommandSourceStack source) {
        return Component.text("Create a new scoreboard");
    }

    @Override
    public String name() {
        return "create";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/sb create <name>");
    }
}
