package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static io.musician101.musiboard.MusiBoard.getPlugin;

public abstract class MusiBoardArgumentType<T> implements ArgumentType<T> {

    @NotNull
    protected MusiScoreboardManager getManager() {
        return getPlugin().getManager();
    }

    @NotNull
    protected MusiScoreboard getScoreboard(@NotNull Player player) {
        return getManager().getScoreboard(player);
    }
}
