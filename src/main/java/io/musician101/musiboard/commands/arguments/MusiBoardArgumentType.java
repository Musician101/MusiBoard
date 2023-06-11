package io.musician101.musiboard.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

import static io.musician101.musiboard.MusiBoard.getPlugin;

public abstract class MusiBoardArgumentType<T> implements ArgumentType<T> {

    @Nonnull
    protected MusiScoreboardManager getManager() {
        return getPlugin().getManager();
    }

    @Nonnull
    protected MusiScoreboard getScoreboard(@Nonnull Player player) {
        return getManager().getScoreboard(player);
    }
}
