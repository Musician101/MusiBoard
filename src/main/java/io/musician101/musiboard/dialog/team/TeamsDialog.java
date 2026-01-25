package io.musician101.musiboard.dialog.team;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.scoreboard.ScoreboardDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class TeamsDialog extends MusiDialog {

    private final MusiScoreboard scoreboard;

    public TeamsDialog(MusiScoreboard scoreboard) {
        super(Component.text("Select Team"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = new ArrayList<>(teamDialogs());
        buttons.add(actionButton(Component.text("New"), showMusiDialogCallback(new NewTeamDialog(scoreboard))));
        return multiActionType(buttons, backButton(showMusiDialogCallback(new ScoreboardDialog(scoreboard))));
    }

    private List<ActionButton> teamDialogs() {
        return scoreboard.getTeams().stream().map(this::teamDialog).toList();
    }

    private ActionButton teamDialog(Team team) {
        return actionButton(team.displayName(), showMusiDialogCallback(new TeamDialog(team, this)));
    }
}
