package io.musician101.musiboard.dialog.objective;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.scoreboard.ScoreboardDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ObjectivesDialog extends MusiDialog {

    final MusiScoreboard scoreboard;

    public ObjectivesDialog(MusiScoreboard scoreboard) {
        super(Component.text("Select Objective"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = new ArrayList<>(scoreboard.getObjectives().stream().map(this::objectiveDialog).toList());
        buttons.add(actionButton(Component.text("New"), showMusiDialogCallback(new NewObjectiveDialog(scoreboard))));
        return DialogType.multiAction(buttons, backButton(showMusiDialogCallback(new ScoreboardDialog(scoreboard))), 2);
    }

    private ActionButton objectiveDialog(Objective objective) {
        return actionButton(objective.displayName(), showMusiDialogCallback(new ObjectiveDialog(objective, this)));
    }
}
