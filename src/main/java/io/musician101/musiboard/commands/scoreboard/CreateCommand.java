package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

class CreateCommand extends MusiBoardCommand implements LiteralCommand {

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<String>() {

            @Override
            public int execute(@Nonnull CommandContext<CommandSender> context) {
                String name = StringArgumentType.getString(context, name());
                boolean success = getManager().registerNewScoreboard(name);
                CommandSender sender = context.getSource();
                if (success) {
                    sendMessage(sender, text(name + " created successfully.", GREEN));
                }
                else {
                    sendMessage(sender, text(name + " already exists.", RED));
                }

                return 1;
            }

            @Nonnull
            @Override
            public String name() {
                return "name";
            }

            @Nonnull
            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canEdit(sender);
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Create a new scoreboard";
    }

    @Nonnull
    @Override
    public String name() {
        return "create";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/sb create <name>";
    }
}
