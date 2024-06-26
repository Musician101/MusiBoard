package io.musician101.musiboard.commands.scoreboard;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.musiboard.commands.MusiBoardCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

class CreateCommand extends MusiBoardCommand implements LiteralCommand {

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new ArgumentCommand<String>() {

            @Override
            public int execute(@NotNull CommandContext<CommandSender> context) {
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

            @NotNull
            @Override
            public String name() {
                return "name";
            }

            @NotNull
            @Override
            public ArgumentType<String> type() {
                return StringArgumentType.word();
            }
        });
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canEdit(sender);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Create a new scoreboard";
    }

    @NotNull
    @Override
    public String name() {
        return "create";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/sb create <name>";
    }
}
