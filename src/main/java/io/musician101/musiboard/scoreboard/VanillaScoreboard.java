package io.musician101.musiboard.scoreboard;

import javax.annotation.Nonnull;
import org.bukkit.Bukkit;

public final class VanillaScoreboard extends MusiScoreboard {

    VanillaScoreboard() {
        super("minecraft", true, Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public void disableObjectiveSave(@Nonnull String objective) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }

    @Override
    public void disableTeamSave(@Nonnull String team) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }

    @Override
    public void saveData(boolean saveData) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }
}
