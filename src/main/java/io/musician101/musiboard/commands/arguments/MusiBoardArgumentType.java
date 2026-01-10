package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
public abstract class MusiBoardArgumentType<T> implements ArgumentType<T> {

    protected MusiScoreboardManager getManager() {
        return getPlugin().getManager();
    }

    protected MusiScoreboard getScoreboard(Player player) {
        return getManager().getScoreboard(player);
    }
}
