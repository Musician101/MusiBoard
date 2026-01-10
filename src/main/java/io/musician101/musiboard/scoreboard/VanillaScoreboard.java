package io.musician101.musiboard.scoreboard;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class VanillaScoreboard extends MusiScoreboard {

    VanillaScoreboard() {
        super("minecraft", true, Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public void disableObjectiveSave(String objective) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }

    @Override
    public void disableTeamSave(String team) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }

    @Override
    public void saveData(boolean saveData) {
        throw new UnsupportedOperationException("Disabling saving data from the Vanilla scoreboard is not supported.");
    }
}
