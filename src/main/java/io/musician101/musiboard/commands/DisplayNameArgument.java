package io.musician101.musiboard.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.util.CheckedFunction;

@NullMarked
public abstract class DisplayNameArgument extends MBCommand implements PaperArgumentCommand.AdventureFormat<Component> {

    public static Component get(CommandContext<CommandSourceStack> context) {
        return context.getArgument("displayName", Component.class);
    }

    @Override
    public String name() {
        return "displayName";
    }

    @Override
    public ArgumentType<Component> type() {
        return ArgumentTypes.component();
    }

    public static DisplayNameArgument withExecutor(CheckedFunction<CommandContext<CommandSourceStack>, Integer, CommandException> executor) {
        return new DisplayNameArgument() {

            @Override
            public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
                return executor.apply(context);
            }
        };
    }
}
